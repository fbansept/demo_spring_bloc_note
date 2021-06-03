package edu.fbansept.demospringblocnote.dao;

import edu.fbansept.demospringblocnote.model.CleHistorique;
import edu.fbansept.demospringblocnote.model.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Repository
public interface HistoriqueDao extends JpaRepository<Historique, CleHistorique> {

    List<Historique> findAllByUtilisateurIdAndNoteId(
            int utilisateurId,
            int noteId);

    @Query("FROM Historique h WHERE h.utilisateur.id = :idUtilisateur")
    List<Historique> listeHistoriqueUtilisateur(@Param("idUtilisateur") int utilisateurId);

    void deleteByUtilisateurIdAndNoteIdAndDateEquals(int utilisateurId, int noteId,Date date);

    @Transactional
    void deleteByUtilisateurIdAndNoteId(int utilisateurId, int noteId);

}
