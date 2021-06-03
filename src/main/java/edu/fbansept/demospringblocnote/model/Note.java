package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.VueHistorique;
import edu.fbansept.demospringblocnote.view.VueNote;
import edu.fbansept.demospringblocnote.view.VueUtilisateur;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({VueUtilisateur.Standard.class, VueNote.Standard.class})
    private Integer id;

    @JsonView({VueUtilisateur.Standard.class, VueNote.Standard.class, VueHistorique.Standard.class})
    private String titre;

    @JsonView(VueNote.Standard.class)
    @ManyToOne
    Utilisateur editeur;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Utilisateur getEditeur() {
        return editeur;
    }

    public void setEditeur(Utilisateur editeur) {
        this.editeur = editeur;
    }
}
