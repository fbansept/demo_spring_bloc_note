package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.VueHistorique;
import edu.fbansept.demospringblocnote.view.VueUtilisateur;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@IdClass(CleHistorique.class)
public class Historique {

    @Id
    private Integer utilisateurId;

    @Id
    @JsonView({VueUtilisateur.AvecHistorique.class})
    private Integer noteId;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({VueUtilisateur.AvecHistorique.class, VueHistorique.Standard.class, VueHistorique.SansNote.class})
    private Date date;

    @ManyToOne
    @MapsId("utilisateur_id")
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @MapsId("note_id")
    @JsonView({VueHistorique.Standard.class})
    @JoinColumn(name = "note_id")
    private Note note;

    @JsonView({VueUtilisateur.AvecHistorique.class, VueHistorique.Standard.class, VueHistorique.SansNote.class})
    private String action;

    public Integer getUtilisateurId() {

        return utilisateurId;
    }

    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}