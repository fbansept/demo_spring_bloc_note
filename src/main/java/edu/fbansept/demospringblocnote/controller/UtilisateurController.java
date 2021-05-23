package edu.fbansept.demospringblocnote.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.demospringblocnote.dao.UtilisateurDao;
import edu.fbansept.demospringblocnote.model.Role;
import edu.fbansept.demospringblocnote.model.Utilisateur;
import edu.fbansept.demospringblocnote.security.JwtUtil;
import edu.fbansept.demospringblocnote.security.UserDetailsCustom;
import edu.fbansept.demospringblocnote.security.UserDetailsServiceCustom;
import edu.fbansept.demospringblocnote.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class UtilisateurController {

    private UtilisateurDao utilisateurDao;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceCustom userDetailsServiceCustom;
    private PasswordEncoder passwordEncoder;

    @Autowired
    UtilisateurController(
            UtilisateurDao utilisateurDao,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UserDetailsServiceCustom userDetailsServiceCustom,
            PasswordEncoder passwordEncoder
    ){
        this.utilisateurDao = utilisateurDao;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceCustom = userDetailsServiceCustom;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/demo/authentification")
    public String authentificationTest(@RequestBody Utilisateur utilisateur) throws Exception {
        return "oups";
    }

    @PostMapping("/authentification")
    public ResponseEntity<String> authentification(@RequestBody Utilisateur utilisateur) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            utilisateur.getPseudo(), utilisateur.getMotDePasse()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Mauvais pseudo / mot de passe");
        }

        UserDetailsCustom userDetails = this.userDetailsServiceCustom.loadUserByUsername(utilisateur.getPseudo());

        return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
    }

    @PostMapping("/inscription")
    public ResponseEntity<String> inscription(@RequestBody Utilisateur utilisateur){

        Optional<Utilisateur> utilisateurDoublon = utilisateurDao.trouverParPseudo(utilisateur.getPseudo());

        if(utilisateurDoublon.isPresent()) {
            return ResponseEntity.badRequest().body("Ce pseudo est déja utilisé");
        } else {

            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));

            Role roleUtilisateur = new Role();
            roleUtilisateur.setId(1);

            utilisateur.getListeRole().add(roleUtilisateur);

            utilisateurDao.saveAndFlush(utilisateur);

            return ResponseEntity.ok(Integer.toString(utilisateur.getId()));
        }
    }

    @PostMapping("/admin/utilisateur")
    public ResponseEntity<String> updateUser(@RequestBody Utilisateur utilisateur){

        Optional<Utilisateur> utilisateurBddOptional = utilisateurDao.trouverParPseudo(utilisateur.getPseudo());

        if(utilisateurBddOptional.isPresent()) {
            Utilisateur utilisateurBdd = utilisateurBddOptional.get();
            utilisateur.setMotDePasse(utilisateurBdd.getMotDePasse());
            utilisateurDao.save(utilisateur);
            return ResponseEntity.ok().body("Utilisateur mis à jour");
        }

        return ResponseEntity.notFound().build();
    }

    @JsonView(CustomJsonView.VueUtilisateur.class)
    @GetMapping("/user/utilisateur-connecte")
    public ResponseEntity<Utilisateur> getInformationUtilisateurConnecte(
            @RequestHeader(value="Authorization") String authorization){
        //la valeur du champs authorization est extrait de l'entête de la requête

        //On supprime la partie "Bearer " de la valeur de l'authorization
        String token = authorization.substring(7);

        //on extrait l'information souhaitée du token
        String username = jwtUtil.getTokenBody(token).getSubject();

        Optional<Utilisateur> utilisateur = utilisateurDao.trouverParPseudo(username);

        if(utilisateur.isPresent()) {
            return ResponseEntity.ok().body(utilisateur.get());
        }

        return ResponseEntity.notFound().build();
    }

    @JsonView(CustomJsonView.VueUtilisateur.class)
    @GetMapping("/admin/utilisateur/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable int id) {

        Optional<Utilisateur> utilisateur = utilisateurDao.findById(id);

        if(utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @JsonView(CustomJsonView.VueUtilisateur.class)
    @GetMapping("/admin/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getUtilisateurs () {

        return ResponseEntity.ok(utilisateurDao.findAll());
    }

    @DeleteMapping("/admin/utilisateur/{id}")
    public ResponseEntity<Integer> deleteUtilisateur (@PathVariable int id) {

        if(utilisateurDao.existsById(id)) {
            utilisateurDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}






