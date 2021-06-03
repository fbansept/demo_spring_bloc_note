package edu.fbansept.demospringblocnote.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class CleHistorique implements Serializable {

    @Column(name = "utilisateur_id")
    Integer utilisateurId;

    @Column(name = "note_id")
    Integer noteId;

    @Column(name = "date")
    Date date;

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
}