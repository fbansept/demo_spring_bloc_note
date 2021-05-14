package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tache {

    @Id
    @JsonView(CustomJsonView.VueUtilisateur.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonView(CustomJsonView.VueUtilisateur.class)
    private boolean termine;

    @JsonView(CustomJsonView.VueUtilisateur.class)
    private String texte;

    @ManyToOne
    private Note note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public boolean isTermine() {
        return termine;
    }

    public void setTermine(boolean termine) {
        this.termine = termine;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }


}
