package com.ouatson.backtontine.admin;

//import com.ouatson.backtontine.SendEmail.EmailSenderService;
import com.ouatson.backtontine.Tontine.Tontine;
import com.ouatson.backtontine.admin.Admin;
import com.ouatson.backtontine.Utilisateurs.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
/*
    @Autowired
    private EmailSenderService emailSenderService;
*/
    private final BCryptPasswordEncoder encode = new BCryptPasswordEncoder(12);

    @PostMapping("/enregistrer")
    public ResponseEntity<Admin> adminRegister(@RequestBody Admin admin) throws Exception {
        Admin nouvelAdmin = adminService.enregisterAdmin(admin);
        if (nouvelAdmin == null) {
            throw new Exception("Un admin avec cet email existe déjà !");
        }
        return new ResponseEntity<>(nouvelAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/connexion")
    public ResponseEntity<Admin> adminLogin(@RequestBody Admin admin) throws Exception {
        Admin bonAdmin = adminService.connexion(admin);
        if (bonAdmin == null) {
            throw new Exception("Les informations fournies ne sont pas correctes !");
        }
        return new ResponseEntity<>(bonAdmin, HttpStatus.OK);
    }

    // Partie pour l'oubli du mot de passe
  /*  @PostMapping("/oubli-password")
    public void oubliPassword(@RequestBody String email) throws Exception {
        Admin existingAdmin = adminService.rechercheAdminByEmail(email);
        if (existingAdmin != null) {
            existingAdmin.setConfirmationToken(UUID.randomUUID().toString());
            adminService.modifierAdmin(existingAdmin);
            String url = "http://localhost:8080/confirm-reset?token=";
            String text = "Pour réinitialiser votre mot de passe, cliquer sur le lien suivant " + url + existingAdmin.getConfirmationToken();
            emailSenderService.sendSimpleEmail(existingAdmin.getEmail(),
                    text,
                    "Confirmation Réinitialisation");
        } else {
            throw new Exception("Aucun compte avec cet email");
        }
    }
*/
    @GetMapping("/confirm-reset")
    public RedirectView confirmation(@RequestParam("token") String token) throws Exception {
        Admin admin = adminService.rechercheAdminByToken(token);
        if (admin == null) {
            throw new Exception("Ce lien n'est pas valide !");
        } else {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://localhost:4200/change-mot-de-passe?email=" + admin.getEmail());
            return redirectView;
        }
    }

    @PutMapping("/reinitialiser/{email}")
    public ResponseEntity<Admin> reinitialiserPassword(@PathVariable("email") String email, @RequestBody String password) throws Exception {
        Admin admin = adminService.rechercheAdminByEmail(email);
        if (admin != null) {
            admin.setPassword(encode.encode(password));
            admin.setConfirmationToken(null);
            adminService.modifierAdmin(admin);
        } else {
            throw new Exception("Aucun compte avec cet email");
        }
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }
    // Fin partie oubli

    @PutMapping("/modifier")
    public ResponseEntity<Admin> adminModify(@RequestBody Admin admin) throws Exception {
        Admin modifAdmin = adminService.modifierAdmin(admin);
        return new ResponseEntity<>(modifAdmin, HttpStatus.OK);
    }

    @GetMapping("/mestontines/{id}")
    public ResponseEntity<Collection<Tontine>> admintontines(@PathVariable("id") Long id) {
        Collection<Tontine> tontines = adminService.mestontines(id);
        return new ResponseEntity<>(tontines, HttpStatus.OK);
    }

    @GetMapping("/adminI/{id}")
    public ResponseEntity<Admin> admin(@PathVariable("id") Long id) {
        Admin admin = adminService.rechercheAdminById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping("/adminE/{email}")
    public ResponseEntity<Admin> searchAdminByEmail(@PathVariable("email") String email) {
        Admin admin = adminService.rechercheAdminByEmail(email);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> adminDelete(@PathVariable("id") Long id) {
        adminService.supprimerAdminById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    //-------------------------Gestion des utilisateurs----------------------------//

    @PostMapping("/enregister-user")
    public ResponseEntity<User> enregisterUser(@RequestBody User user) {
        User newUser = adminService.enregisterUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/recherche-user/{email}")
    public ResponseEntity<User> rechercheUserByEmail(@PathVariable("email") String email) {
        User user = adminService.rechercheUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/recherche-user-id/{id}")
    public ResponseEntity<User> rechercheUserById(@PathVariable("id") Long id) {
        User user = adminService.rechercheUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/recherche-user-token/{confirmToken}")
    public ResponseEntity<User> rechercheUserByToken(@PathVariable("confirmToken") String confirmToken) {
        User user = adminService.rechercheUserByToken(confirmToken);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/modifier-user")
    public ResponseEntity<User> modifierUser(@RequestBody User user) {
        User modifiedUser = adminService.modifierUser(user);
        return new ResponseEntity<>(modifiedUser, HttpStatus.OK);
    }

    @GetMapping("/mestontines-user/{id}")
    public ResponseEntity<Collection<Tontine>> mestontinesUser(@PathVariable("id") Long id) {
        Collection<Tontine> tontines = adminService.mestontines(id);
        return new ResponseEntity<>(tontines, HttpStatus.OK);
    }

    @DeleteMapping("/supprimer-user/{id}")
    public ResponseEntity<?> supprimerUserById(@PathVariable("id") Long id) {
        adminService.supprimerUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/accepter-utilisateur/{tontineId}/{userId}")
    public ResponseEntity<?> accepterUtilisateur(
            @PathVariable("tontineId") Long tontineId,
            @PathVariable("userId") Long userId) {
        adminService.accepterUtilisateur(tontineId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/refuser-utilisateur/{tontineId}/{userId}")
    public ResponseEntity<?> refuserUtilisateur(
            @PathVariable("tontineId") Long tontineId,
            @PathVariable("userId") Long userId) {
        adminService.refuserUtilisateur(tontineId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    //------------------------------------Gestion des tontines----------------------------//

    @PostMapping("/creer-tontine/{adminId}")
    public ResponseEntity<Tontine> creerTontine(@PathVariable("adminId") Long adminId, @RequestBody Tontine tontine) {
        Tontine nouvelleTontine = adminService.creerTontine(adminId, tontine);
        return new ResponseEntity<>(nouvelleTontine, HttpStatus.CREATED);
    }

    @PutMapping("/modifier-tontine/{adminId}")
    public ResponseEntity<Tontine> modifierTontine(@PathVariable("adminId") Long adminId, @RequestBody Tontine tontine) {
        Tontine modifTontine = adminService.modifierTontine(adminId, tontine);
        return new ResponseEntity<>(modifTontine, HttpStatus.OK);
    }

    @DeleteMapping("/supprimer-tontine/{adminId}/{tontineId}")
    public ResponseEntity<?> supprimerTontine(@PathVariable("adminId") Long adminId, @PathVariable("tontineId") Long tontineId) {
        adminService.supprimerTontine(adminId, tontineId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/toutes-mes-tontines/{adminId}")
    public ResponseEntity<Collection<Tontine>> toutesMesTontines(@PathVariable("adminId") Long adminId) {
        Collection<Tontine> tontines = adminService.toutesMesTontines(adminId);
        return new ResponseEntity<>(tontines, HttpStatus.OK);
    }




}
