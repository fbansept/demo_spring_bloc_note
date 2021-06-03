package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.VueUtilisateur;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class NoteListe extends Note{

    @JsonView(VueUtilisateur.Standard.class)
    private boolean trierParEtat;

    @OneToMany(mappedBy = "note")
    @JsonView(VueUtilisateur.Standard.class)
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
