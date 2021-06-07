package edu.fbansept.demospringblocnote.controller;

import edu.fbansept.demospringblocnote.dao.NoteListeDao;
import edu.fbansept.demospringblocnote.dao.TacheDao;
import edu.fbansept.demospringblocnote.model.NoteListe;
import edu.fbansept.demospringblocnote.model.Tache;
import edu.fbansept.demospringblocnote.model.Utilisateur;
import edu.fbansept.demospringblocnote.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class NoteListeController {

    NoteListeDao noteListeDao;
    TacheDao tacheDao;
    JwtUtil jwtUtil;

    @Autowired
    NoteListeController(NoteListeDao noteListeDao, TacheDao tacheDao, JwtUtil jwtUtil){
        this.noteListeDao = noteListeDao;
        this.tacheDao = tacheDao;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user/noteListe/{id}")
    public ResponseEntity<NoteListe> getNoteListe(@PathVariable int id) {

        Optional<NoteListe> noteListe = noteListeDao.findById(id);

        if(noteListe.isPresent()) {
            return ResponseEntity.ok(noteListe.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/user/noteListe")
    public ResponseEntity<String> addNoteListe (
            @RequestBody NoteListe noteListe,
            @RequestHeader(value="Authorization") String authorization) {

        String token = authorization.substring(7);
        Integer idUtilisateur = jwtUtil.getTokenBody(token).get("id",Integer.class);

        if(noteListe.getId() != null) {

            //on récupère la note actuelle dans la BDD
            Optional<NoteListe> ancienneNoteListe = noteListeDao.findById(noteListe.getId());

            //si la note n'existe pas
            if (!ancienneNoteListe.isPresent()) {

                return ResponseEntity.badRequest().body("La liste n'existe pas");

            //si il n'est pas l'auteur de la note
            } else if (ancienneNoteListe.get().getEditeur().getId() != idUtilisateur) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous ne pouvez pas éditer cette note");
            } else {

                List<Integer> listeIdTacheAsauvegarder =
                        noteListe.getListeTache().stream()
                                .map(tache -> tache.getId()).collect(Collectors.toList());

                //si la note existait déjà, on supprime toutes les taches
                //qui ne sont pas à enregistrer
                for(Tache tache : ancienneNoteListe.get().getListeTache()){
                    if(!listeIdTacheAsauvegarder.contains(tache.getId())) {
                        tacheDao.delete(tache);
                    }
                }
            }
        }

        //on indique que c'est lui l'editeur de la note
        noteListe.setEditeur(new Utilisateur(idUtilisateur));

        NoteListe noteListeBdd = noteListeDao.saveAndFlush(noteListe);

        for (Tache tache : noteListe.getListeTache()) {
            tache.setNote(noteListeBdd);
        }

        tacheDao.saveAll(noteListe.getListeTache());

        return ResponseEntity.created(
                URI.create("/user/noteListe/" + noteListe.getId())
        ).build();
    }

    @DeleteMapping("/user/noteListe/{id}")
    public ResponseEntity<Integer> deleteNoteListe (@PathVariable int id) {

        if(noteListeDao.existsById(id)) {
            noteListeDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}






