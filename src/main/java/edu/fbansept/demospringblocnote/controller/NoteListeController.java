package edu.fbansept.demospringblocnote.controller;

import edu.fbansept.demospringblocnote.dao.NoteListeDao;
import edu.fbansept.demospringblocnote.model.NoteListe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@CrossOrigin
public class NoteListeController {

    NoteListeDao noteListeDao;

    @Autowired
    NoteListeController(NoteListeDao noteListeDao){
        this.noteListeDao = noteListeDao;
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
    public ResponseEntity<String> addNoteListe (@RequestBody NoteListe noteListe) {

        noteListe = noteListeDao.saveAndFlush(noteListe);
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






