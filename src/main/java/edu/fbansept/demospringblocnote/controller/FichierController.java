package edu.fbansept.demospringblocnote.controller;

import edu.fbansept.demospringblocnote.dao.NoteListeDao;
import edu.fbansept.demospringblocnote.dao.TacheDao;
import edu.fbansept.demospringblocnote.security.JwtUtil;
import edu.fbansept.demospringblocnote.utils.FichierService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class FichierController {

    NoteListeDao noteListeDao;
    TacheDao tacheDao;
    JwtUtil jwtUtil;
    private FichierService fichierService;

    @Autowired
    FichierController(
            NoteListeDao noteListeDao,
            TacheDao tacheDao,
            JwtUtil jwtUtil,
            FichierService fichierService){

        this.noteListeDao = noteListeDao;
        this.tacheDao = tacheDao;
        this.jwtUtil = jwtUtil;
        this.fichierService = fichierService;
    }

    @ResponseBody
    @GetMapping(value = "/test/download/{nomDeFichier}")
    public ResponseEntity<byte[]> getImageAsResource(@PathVariable String nomDeFichier) {

        try {

            String mimeType = "";

            //verification nom de fichier
            if(!fichierService.nomFichierValide(nomDeFichier)) {
                System.out.println("nom de fichier incorrect, uniquement : - _ . lettres et chiffres (ex : pas de slash)");
                return ResponseEntity.notFound().build();
            }

            try {
                mimeType = Files.probeContentType(new File(nomDeFichier).toPath());


            //le fichier a une extension inconnue
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur : le fichier a une extension inconnue ou n'existe pas");
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();

            //note si mimeType est null c'est qe le fichier n'a pas d'extension ou n'existe pas
            if(mimeType != null) {
                headers.setContentType(MediaType.valueOf(mimeType));
            }

            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            byte[] media = fichierService.getFileFromUploadFolder(nomDeFichier);

            return new ResponseEntity<>(media, headers, HttpStatus.OK);


        } catch (FileNotFoundException e) {
            //Le fichier nom du fichier comporte un caractère non accepté ou le dossier d'upload est mal configuré
            //Voir application.properties : dossier.upload

            e.printStackTrace();
            System.out.println("Erreur : Le fichier est introuvable");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Le nom du fichier comporte un caractère non accepté ou le dossier d'upload est mal configuré");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }


    /**
     * Afin de maximiser la compatibilité l'encodage en base64 se fait "URL SAFE".
     * L'encodage doit se faire en remplaçant les caractères / par _ et + par -.
     *
     * Le type mime du fichier doit précéder la chaine envoyée au format :
     * data:image/jpeg;base64,..............................
     *
     */
    @PostMapping(value = "/test/upload-fichier-base64")
    @ResponseBody
    public ResponseEntity<String> uploadBase64File(@RequestBody String chaineBase64) {
        StringBuffer fileName = new StringBuffer();
        fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));

        String mimeType;
        Pattern pattern = Pattern.compile("data:(.*?);base64,");
        Matcher matcher = pattern.matcher(chaineBase64);
        if(matcher.find()) {
            mimeType = matcher.group(1);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body("prefix : data:TYPE_MIME;base64, manquant (ex : data:image/jpeg;base64,..........)");
        }

        //Voir : https://developer.mozilla.org/fr/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        List<String> mimeTypeAccepte = Arrays.asList(
                "image/bmp", "image/gif", "image/png", "image/jpeg", "text/html", "application/pdf");

        //le fichier n'est pas un type autorisé
        if(!mimeTypeAccepte.contains(mimeType)) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body("Le fichier n'est pas un type autorisé");
        } else {
            switch (mimeType) {
                case "image/bmp" :  fileName.append(".bmp"); break;
                case "image/gif" :  fileName.append(".gif"); break;
                case "image/png" :  fileName.append(".png"); break;
                case "image/jpeg" :  fileName.append(".jpg"); break;
                case "text/html" :  fileName.append(".html"); break;
                case "application/pdf" :  fileName.append(".pdf"); break;
            }
        }

        chaineBase64 = chaineBase64.replace("data:" + mimeType + ";base64,", "");

        byte[] fileBytes = Base64.getUrlDecoder().decode(chaineBase64);
        try {
            fichierService.uploadToLocalFileSystem(fileBytes, fileName.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Sauvegarde échouée");
        }

        return ResponseEntity.ok().body("Sauvegarde réussie");
    }

    @PostMapping("/test/upload-fichier")
    public ResponseEntity<String> uploadMultipartFile(@RequestParam("file") MultipartFile file) {
        try {

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());

            List<String> extensionAccepte = Arrays.asList(
                    "bmp", "gif", "png","jpg", "jpeg", "html", "pdf");

            if(!extensionAccepte.contains(extension)) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body("L'extension de ce fichier n'est pas prise en compte");
            }

            StringBuffer fileName = new StringBuffer();
            fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));

            fichierService.uploadToLocalFileSystem(file, fileName + "." + extension);

            return ResponseEntity.status(HttpStatus.OK).body("Sauvegarde réussie");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Sauvegarde échouée");
        }
    }
}






