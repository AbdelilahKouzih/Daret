package com.ouatson.backtontine.admin;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ouatson.backtontine.Problemes.Impayes.Impayes;
import com.ouatson.backtontine.Problemes.Signals.Signals;
import com.ouatson.backtontine.Problemes.Vols.Vols;
import com.ouatson.backtontine.Tontine.Tontine;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Admin implements Serializable {
    public enum Sexe{
        Masculin,Feminin
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaiss;
    private String adresse;
    private String numTel;
    private String profession;
    private String password;
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    //private String status_matrimonial;
    private String confirmationToken;
    @OneToMany(mappedBy = "proprietaire")
    private Collection<Tontine> tontine;
    @OneToMany(mappedBy = "utilisateur")
    private Collection<Signals> signals;
    @OneToMany(mappedBy = "owner")
    private Collection<Vols> vols;
    @OneToMany(mappedBy = "proprio")
    private Collection<Impayes> impayes;

    public Admin() {
    }

    public Admin(Long id,
                String nom,
                String prenom,
                String email,
                LocalDate dateNaiss,
                String adresse,
                String numTel,
                String profession,
                String password,
                Sexe sexe,
                String confirmationToken) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateNaiss = dateNaiss;
        this.adresse = adresse;
        this.numTel = numTel;
        this.profession = profession;
        this.password = password;
        this.sexe = sexe;
        this.confirmationToken = confirmationToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }



    public String getConfirmationToken() { return confirmationToken; }

    public void setConfirmationToken(String confirmationToken) { this.confirmationToken = confirmationToken; }

    public Collection<Tontine> getTontine() {
        return tontine;
    }

    public void setTontine(Collection<Tontine> tontine) {
        this.tontine = tontine;
    }

    public Collection<Signals> getSignals() { return signals; }

    public void setSignals(Collection<Signals> signals) { this.signals = signals; }

    public Collection<Vols> getVols() { return vols; }

    public void setVols(Collection<Vols> vols) { this.vols = vols; }

    public Collection<Impayes> getImpayes() { return impayes; }

    public void setImpayes(Collection<Impayes> impayes) { this.impayes = impayes; }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", dateNaiss=" + dateNaiss +
                ", adresse='" + adresse + '\'' +
                ", numTel='" + numTel + '\'' +
                ", profession='" + profession + '\'' +
                ", password='" + password + '\'' +
                ", sexe=" + sexe +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", tontine=" + tontine +
                ", signals=" + signals +
                ", vols=" + vols +
                ", impayes=" + impayes +
                '}';
    }
}