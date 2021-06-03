package edu.fbansept.demospringblocnote.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.dao.HistoriqueDao;
import edu.fbansept.demospringblocnote.model.Historique;
import edu.fbansept.demospringblocnote.view.VueHistorique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@CrossOrigin
public class TestController {

    private HistoriqueDao historiqueDao;

    @Autowired
    TestController(HistoriqueDao historiqueDao){
        this.historiqueDao = historiqueDao;
    }


    @GetMapping("/test/test1")
    @JsonView(VueHistorique.Standard.class)
    public List<Historique> test () {
        return historiqueDao.listeHistoriqueUtilisateur(1);
    }

    @GetMapping("/test/test2")
    @JsonView(VueHistorique.SansNote.class)
    public List<Historique> test2 () {
        return historiqueDao.findAllByUtilisateurIdAndNoteId(1,1);
    }

    @GetMapping("/test/test3")
    public ResponseEntity<Object> test3 () {

        Historique historique = new Historique();
        historique.setDate(new Date());
        historique.setUtilisateurId(1);
        historique.setNoteId(1);
        historique.setAction("Nouvel historique");

        historiqueDao.save(historique);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/test4")
    public ResponseEntity<Object> test4 () throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        historiqueDao.deleteByUtilisateurIdAndNoteIdAndDateEquals(
                1,1,simpleDateFormat.parse("2021-05-04 00:00:00"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/test5")
    public ResponseEntity<Object> test5 () throws ParseException {

        historiqueDao.deleteByUtilisateurIdAndNoteId(1,1);

        return ResponseEntity.ok().build();
    }
}






