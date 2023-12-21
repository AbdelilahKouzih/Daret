package com.ouatson.backtontine.admin;

import com.ouatson.backtontine.Tontine.Tontine;
import com.ouatson.backtontine.Tontine.TontineService;
import com.ouatson.backtontine.admin.Admin;
import com.ouatson.backtontine.Utilisateurs.AdminNotFoundException;
import com.ouatson.backtontine.Utilisateurs.User;
import com.ouatson.backtontine.Utilisateurs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class AdminService {
    BCryptPasswordEncoder encode = new BCryptPasswordEncoder(12);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    UserService userService;


    @Autowired
    private TontineService tontineService;

    //-------------------------Gestion des admins----------------------------//
    public Admin enregisterAdmin(Admin admin){
        Admin adminObj = adminRepository.findByEmail(admin.getEmail());
        if (adminObj != null) {
            return null;
        } else {
            admin.setPassword(encode.encode(admin.getPassword()));
            return adminRepository.save(admin);
        }
    }

    public Admin connexion(Admin admin){
        Admin existingAdmin = adminRepository.findByEmail(admin.getEmail());
        if (existingAdmin != null) {
            if (encode.matches(admin.getPassword(), existingAdmin.getPassword())) {
                return existingAdmin;
            } else return null;
        } else return null;
    }

    public Admin rechercheAdminByEmail(String email){
        return adminRepository.findByEmail(email);
    }

    public Admin rechercheAdminById(Long id){
        return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException("Admin d'identifiant "+id+" non trouve !"));
    }

    public Admin rechercheAdminByToken(String confirmToken) {
        return adminRepository.findByConfirmationToken(confirmToken);
    }
    public Admin modifierAdmin(Admin admin) throws Exception {
        // Vérifier si l'administrateur existe par e-mail
        Admin existingAdmin = rechercheAdminByEmail(admin.getEmail());
        if (existingAdmin == null) {
            throw new Exception("L'administrateur avec l'e-mail " + admin.getEmail() + " n'existe pas.");
        }
        existingAdmin.setNom(admin.getNom());
        existingAdmin.setPrenom(admin.getPrenom());
        existingAdmin.setDateNaiss(admin.getDateNaiss());
        existingAdmin.setAdresse(admin.getAdresse());
        existingAdmin.setNumTel(admin.getNumTel());
        existingAdmin.setProfession(admin.getProfession());
        existingAdmin.setPassword(encode.encode(admin.getPassword()));
        existingAdmin.setSexe(admin.getSexe());

        // Mettre à jour l'administrateur existant dans la base de données
        return adminRepository.save(existingAdmin);
    }




    public Collection<Tontine> admintontines(Long id){
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException("Admin d'identifiant "+id+" non trouve !"));
        return admin.getTontine();
    }

    @Transactional
    public void supprimerAdminById(Long id){
        adminRepository.deleteById(id);
    }



    //-------------------------Gestion des utilisateurs----------------------------//

    public User enregisterUser(User user){
      return  userService.enregisterUser(user);
    }

    public User rechercheUserByEmail(String email){


        return userService.rechercheUserByEmail(email);

    }

    public User rechercheUserById(Long id){
        return userService.rechercheUserById(id);
    }

    public User rechercheUserByToken(String confirmToken) {
        return userService.rechercheUserByToken(confirmToken);
    }

    public User modifierUser(User user){

        return userService.modifierUser(user);
    }

    public Collection<Tontine> mestontines(Long id){
        return userService.mestontines(id);
    }

    @Transactional
    public void supprimerUserById(Long id){
        userService.supprimerUserById(id);
    }


    @Transactional
    public void accepterUtilisateur(Long tontineId, Long userId) {
        User user = rechercheUserById(userId);

        if (user != null) {
            // Ajouter l'utilisateur à la tontine
            tontineService.accepterUtilisateur(tontineId, user);

        } else {
            throw new AdminNotFoundException("L'utilisateur d'identifiant " + userId + " non trouvé !");
        }
    }

    @Transactional
    public void refuserUtilisateur(Long tontineId, Long userId) {
        User user = rechercheUserById(userId);

        if (user != null) {
            // Refuser l'utilisateur de la tontine
            tontineService.refuserUtilisateur(tontineId, user);
        } else {
            throw new AdminNotFoundException("L'utilisateur d'identifiant " + userId + " non trouvé !");
        }
    }


    //------------------------------------Gestion des tontines----------------------------//


    // Ajouter une nouvelle tontine
    public Tontine creerTontine(Long adminId, Tontine tontine) {
        Admin admin = rechercheAdminById(adminId);
        if (admin != null) {
            return tontineService.ajoutTontine(tontine);
        } else {
            throw new AdminNotFoundException("Admin d'identifiant " + adminId + " non trouvé !");
        }
    }

    // Modifier une tontine existante
    public Tontine modifierTontine(Long adminId, Tontine tontine) {
        Admin admin = rechercheAdminById(adminId);
        if (admin != null) {
            return tontineService.modifierTontine(tontine);
        } else {
            throw new AdminNotFoundException("Admin d'identifiant " + adminId + " non trouvé !");
        }
    }

    // Supprimer une tontine
    public void supprimerTontine(Long adminId, Long tontineId) {
        Admin admin = rechercheAdminById(adminId);
        if (admin != null) {
            tontineService.supprimerTontine(tontineId);
        } else {
            throw new AdminNotFoundException("Admin d'identifiant " + adminId + " non trouvé !");
        }
    }

    // Afficher toutes les tontines associées à l'admin
    public Collection<Tontine> toutesMesTontines(Long adminId) {
        Admin admin = rechercheAdminById(adminId);
        if (admin != null) {
            return tontineService.toutesMesTontinesAdmin(admin);
        } else {
            throw new AdminNotFoundException("Admin d'identifiant " + adminId + " non trouvé !");
        }
    }


}
