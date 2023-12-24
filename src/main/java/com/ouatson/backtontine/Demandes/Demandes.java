package com.ouatson.backtontine.Demandes;

import com.ouatson.backtontine.Tontine.Tontine;
import com.ouatson.backtontine.Utilisateurs.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Demandes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String email;
    private String nom_complet;
    private String probleme;
    private String telprobleme;
    private Boolean action;
    private Boolean acceptation;
    private Date dateDemande;
    private Date dateAction;

    @ManyToOne
    private Tontine tontine;

    @ManyToMany
    @JoinTable(
            name = "demandes_utilisateurs",
            joinColumns = @JoinColumn(name = "demande_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    private List<User> utilisateurs;

    public Demandes() {
    }

    public Demandes(Long id, String email, String nom_complet, String probleme, String telprobleme, Boolean action, Boolean acceptation, Date dateDemande, Date dateAction, Tontine tontine, List<User> utilisateurs) {
        this.id = id;
        this.email = email;
        this.nom_complet = nom_complet;
        this.probleme = probleme;
        this.telprobleme = telprobleme;
        this.action = action;
        this.acceptation = acceptation;
        this.dateDemande = dateDemande;
        this.dateAction = dateAction;
        this.tontine = tontine;
        this.utilisateurs = utilisateurs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }

    public String getProbleme() {
        return probleme;
    }

    public void setProbleme(String probleme) {
        this.probleme = probleme;
    }

    public String getTelprobleme() {
        return telprobleme;
    }

    public void setTelprobleme(String telprobleme) {
        this.telprobleme = telprobleme;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public Boolean getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(Boolean acceptation) {
        this.acceptation = acceptation;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Date getDateAction() {
        return dateAction;
    }

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    public Tontine getTontine() {
        return tontine;
    }

    public void setTontine(Tontine tontine) {
        this.tontine = tontine;
    }

    public List<User> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<User> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
}
