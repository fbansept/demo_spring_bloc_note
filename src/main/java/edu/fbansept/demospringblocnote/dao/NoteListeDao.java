package edu.fbansept.demospringblocnote.dao;

import edu.fbansept.demospringblocnote.model.NoteListe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteListeDao extends JpaRepository<NoteListe, Integer> {

}
