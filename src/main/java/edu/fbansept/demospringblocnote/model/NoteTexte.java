package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.VueUtilisateur;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class NoteTexte extends Note{

    @JsonView(VueUtilisateur.Standard.class)
    @Column(columnDefinition = "TEXT")
    private String texte;

    @JsonView(VueUtilisateur.Standard.class)
    private String url;

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
