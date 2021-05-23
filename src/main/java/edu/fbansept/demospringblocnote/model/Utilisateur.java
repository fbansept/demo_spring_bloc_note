package edu.fbansept.demospringblocnote.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.view.CustomJsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Utilisateur {

    public Utilisateur() {
    }

    public Utilisateur(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CustomJsonView.VueUtilisateur.class)
    private int id;

    @Column(nullable = false, length = 50)
    @JsonView(CustomJsonView.VueUtilisateur.class)
    private String pseudo;

    private String motDePasse;

    @JsonView(CustomJsonView.VueUtilisateur.class)
    @OneToMany(mappedBy = "editeur")
    private List<Note> listeNote;

    @ManyToMany
    @JsonView({CustomJsonView.VueUtilisateur.class})
    @JoinTable(
            name = "utilisateur_role",
            joinColumns = @JoinColumn(name = "utilisateur_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> listeRole = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Set<Role> getListeRole() {
        return listeRole;
    }

    public void setListeRole(Set<Role> listeRole) {
        this.listeRole = listeRole;
    }

    public List<Note> getListeNote() {
        return listeNote;
    }

    public void setListeNote(List<Note> listeNote) {
        this.listeNote = listeNote;
    }
}
