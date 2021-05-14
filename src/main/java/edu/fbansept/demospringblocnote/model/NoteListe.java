package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class NoteListe extends Note{

    @JsonView(CustomJsonView.VueUtilisateur.class)
    private boolean trierParEtat;

    @OneToMany(mappedBy = "note")
    @JsonView(CustomJsonView.VueUtilisateur.class)
    Set<Tache> listeTache = new HashSet<>();

    public boolean isTrierParEtat() {
        return trierParEtat;
    }

    public void setTrierParEtat(boolean trierParEtat) {
        this.trierParEtat = trierParEtat;
    }

    public Set<Tache> getListeTache() {
        return listeTache;
    }

    public void setListeTache(Set<Tache> listeTache) {
        this.listeTache = listeTache;
    }
}
