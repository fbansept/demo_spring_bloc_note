package edu.fbansept.demospringblocnote.dao;

import edu.fbansept.demospringblocnote.model.NoteTexte;
import edu.fbansept.demospringblocnote.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteTexteDao extends JpaRepository<NoteTexte, Integer> {

}