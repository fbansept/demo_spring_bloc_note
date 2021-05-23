package edu.fbansept.demospringblocnote.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fbansept.demospringblocnote.dao.NoteTexteDao;
import edu.fbansept.demospringblocnote.dao.UtilisateurDao;
import edu.fbansept.demospringblocnote.model.NoteTexte;
import edu.fbansept.demospringblocnote.model.Utilisateur;
import edu.fbansept.demospringblocnote.security.JwtUtil;
import edu.fbansept.demospringblocnote.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Optional;

@RestController
@CrossOrigin
public class NoteTexteController {

    private NoteTexteDao noteTexteDao;
    private UtilisateurDao utilisateurDao;
    private JwtUtil jwtUtil;

    @Autowired
    NoteTexteController(
            NoteTexteDao noteTexteDao,
            UtilisateurDao utilisateurDao,
            JwtUtil jwtUtil){
        this.noteTexteDao = noteTexteDao;
        this.utilisateurDao = utilisateurDao;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/test/android")
    public ResponseEntity<String> getNoteTexte() {

        return ResponseEntity.badRequest().body("nope");
    }

    @JsonView(CustomJsonView.VueNote.class)
    @GetMapping("/test/noteTexte/{id}")
    public ResponseEntity<NoteTexte> getNoteTexte(@PathVariable int id) {

        Optional<NoteTexte> noteTexte = noteTexteDao.findById(id);

        if(noteTexte.isPresent()) {
            return ResponseEntity.ok(noteTexte.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/user/noteTexte")
    public ResponseEntity<String> addNoteTexte (
            @RequestBody NoteTexte noteTexte,
            @RequestHeader(value="Authorization") String authorization) {

        String token = authorization.substring(7);
        Integer idUtilisateur = jwtUtil.getTokenBody(token).get("id",Integer.class);

        if(noteTexte.getId() == null) {
            noteTexte.setEditeur(new Utilisateur(idUtilisateur));
            noteTexte = noteTexteDao.saveAndFlush(noteTexte);

            return ResponseEntity.created(
                    URI.create("/user/noteTexte/" + noteTexte.getId())
            ).build();
        } else {
            Optional<NoteTexte> noteTexteBdd = noteTexteDao.findById(noteTexte.getId());

            if (noteTexteBdd.isPresent()){
                if(noteTexteBdd.get().getEditeur().getId() == idUtilisateur) {
                    noteTexteBdd.get().setTitre(noteTexte.getTitre());
                    noteTexteBdd.get().setTexte(noteTexte.getTexte());
                    noteTexteDao.save(noteTexteBdd.get());
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.badRequest().body("Vous n'avez pas les droits pour modifier cette note");
                }
            } else {
                return ResponseEntity.badRequest().body("Cette note n'existe pas ou a été supprimée");
            }
        }
    }

    @PostMapping("/user/noteTexte-avec-fichier")
    public ResponseEntity<String> addNoteTexte (
            @RequestParam("file") MultipartFile file,
            @RequestParam("note") String noteTexteJson) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            NoteTexte noteTexte = mapper.readValue(noteTexteJson, NoteTexte.class);

            noteTexte = noteTexteDao.saveAndFlush(noteTexte);
            return ResponseEntity.created(
                    URI.create("/user/noteTexte/" + noteTexte.getId())
            ).build();

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/user/noteTexte/{id}")
    public ResponseEntity<Integer> deleteNoteTexte (@PathVariable int id) {

        if(noteTexteDao.existsById(id)) {
            noteTexteDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}






