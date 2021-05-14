package edu.fbansept.demospringblocnote.controller;

import edu.fbansept.demospringblocnote.dao.TacheDao;
import edu.fbansept.demospringblocnote.model.Tache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class TacheController {

    TacheDao tacheDao;

    @Autowired
    TacheController(TacheDao tacheDao){
        this.tacheDao = tacheDao;
    }

    @PostMapping("/user/tache")
    public ResponseEntity<String> addTache (@RequestBody Tache tache) {

        tache = tacheDao.saveAndFlush(tache);
        return ResponseEntity.created(
                URI.create("/user/tache/" + tache.getId())
        ).build();
    }

    @DeleteMapping("/user/tache/{id}")
    public ResponseEntity<Integer> deleteTache (@PathVariable int id) {

        if(tacheDao.existsById(id)) {
            tacheDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}






