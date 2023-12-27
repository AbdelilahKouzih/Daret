package com.ouatson.backtontine.Tontine;
import com.ouatson.backtontine.Demandes.Demandes;
import com.ouatson.backtontine.Participants.Participant;
import com.ouatson.backtontine.Participants.ParticipantService;
import com.ouatson.backtontine.Participation.Participation;
import com.ouatson.backtontine.Participation.ParticipationService;
import com.ouatson.backtontine.Utilisateurs.UserService;
import com.ouatson.backtontine.admin.Admin;
import com.ouatson.backtontine.Utilisateurs.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class TontineService {

    @Autowired
    private TontineRepository tontineRepository;

    @Autowired
    ParticipantService participantService;

    @Autowired
    UserService userService;
    @Autowired
    ParticipationService participationService;
    public List<Tontine> toutesMesTontines(Participant participant){
        List<Tontine> all = tontineRepository.findAll();
        List<Tontine> mine = new ArrayList<>();

        all.forEach(tontine -> {
            Collection<Participation> participations = tontine.getParticipations();

            participations.forEach(participation -> {
                List<Participant> participants = participation.getParticipants();

                participants.forEach(participantT -> {
                    if (participantT.getEmail().equals(participant.getEmail())) {
                        mine.add(tontine);
                    }
                });
            });
        });

        return mine;
    }



    public List<Tontine> toutesMesTontinesAdmin(Admin admin){
        List<Tontine> all = tontineRepository.findAll();
        List<Tontine> mine = new ArrayList<>();

        all.forEach(tontine -> {
            Collection<Participation> participations = tontine.getParticipations();

            participations.forEach(participation -> {
                List<Participant> participants = participation.getParticipants();

                participants.forEach(participant -> {
                    if (admin.getEmail().equals(participant.getEmail())) {
                        mine.add(tontine);
                    }
                });
            });
        });

        return mine;
    }

    public Tontine ajoutTontine(Tontine tontine){
        UUID uuid = UUID.randomUUID();
        tontine.setCode(uuid.toString());
        return tontineRepository.save(tontine);
    }

    public Tontine modifierTontine(Tontine tontine){
        return tontineRepository.save(tontine);
    }

    public Tontine rechercheTontine(Long id){
        return tontineRepository.findById(id).orElseThrow(() -> new TontineNotFoundException("Aucune tontine trouvée !"));
    }

    public Tontine rechercheTontineByCode(String code){
        return tontineRepository.findByCode(code).orElseThrow(() -> new TontineNotFoundException("Aucune tontine trouvée !"));
    }

    @Transactional
    public void supprimerTontine(Long id){
        tontineRepository.deleteById(id);
    }



/*
    @Transactional
    public void accepterUtilisateur(Long tontineId, User user) {
        Tontine tontine = rechercheTontine(tontineId);
        if (tontine != null) {
            // Vérifier si l'utilisateur n'est pas déjà participant
            boolean isUserParticipant = tontine.getParticipant().stream()
                    .anyMatch(participant -> user.getEmail().equals(participant.getEmail()));

            if (!isUserParticipant) {
                // Ajouter l'utilisateur comme participant à la tontine
                Participant participant = new Participant();
                participant.setEmail(user.getEmail());
                participant.setTontine(tontine);
                participantService.enregistrerPart(participant);
            }

        } else {
            throw new TontineNotFoundException("Tontine d'identifiant " + tontineId + " non trouvée !");
        }
    }

    @Transactional
    public void refuserUtilisateur(Long tontineId, User user) {
        Tontine tontine = rechercheTontine(tontineId);

        if (tontine != null) {
            // Retirer l'utilisateur comme participant de la tontine s'il existe
            tontine.getParticipant().removeIf(participant -> user.getEmail().equals(participant.getEmail()));

            // Ajouter d'autres logiques nécessaires pour refuser l'utilisateur
        } else {
            throw new TontineNotFoundException("Tontine d'identifiant " + tontineId + " non trouvée !");
        }
    }



 */@Transactional
public void accepterDemande(Demandes demande) throws Exception {
    Tontine tontine = rechercheTontine(demande.getTontine().getId());

    if (tontine == null) {
        throw new TontineNotFoundException("Tontine d'identifiant " + demande.getTontine().getId() + " non trouvée !");
    }

    List<User> utilisateurs = demande.getUtilisateurs();
    int currentNombrePart = tontine.getParticipations().size();
    if (currentNombrePart >= tontine.getNombrePart()) {
        throw new TontineNotFoundException("Le nombre total de participants atteint la limite autorisée.");
    }

    Participation participation = new Participation();
    participation.setTontine(tontine);

    int nombreDoublementMontant = tontine.getNombreDoublementMontant();

    long  newMontant = (nombreDoublementMontant + currentNombrePart) * tontine.getMontant();
    long  montantTotal = tontine.getMontant() * tontine.getNombrePart();

    if (utilisateurs.isEmpty()) {
        User utilisateur = userService.rechercheUserByEmail(demande.getEmail());
        if (utilisateur != null) {
            if (tontineContainsParticipant(tontine, utilisateur)) {
                throw new Exception("L'utilisateur participe déjà à cette tontine.");
            }

            Participant participant = createUserParticipant(utilisateur, tontine);

            if (nombreDoublementMontant > 0 &&  newMontant < montantTotal) {
                for (int i = 0; i < nombreDoublementMontant; i++) {
                    participation.getParticipants().add(participant);
                }
            } else {
                participation.getParticipants().add(participant);
            }
        }
    } else {


        for (User utilisateur : utilisateurs) {
            Participant participant = createUserParticipant(utilisateur, tontine);
            participation.getParticipants().add(participant);
        }
    }

    // Save the participation
    participationService.enregistrerParticipation(participation);

    // Add the participation to the tontine
    tontine.getParticipations().add(participation);
}

    private boolean tontineContainsParticipant(Tontine tontine, User utilisateur) {
        // Check if the user already participates in the tontine
        return tontine.getParticipations()
                .stream()
                .anyMatch(participation -> participation.getParticipants()
                        .stream()
                        .anyMatch(p -> p.getEmail().equals(utilisateur.getEmail())));
    }

    private Participant createUserParticipant(User utilisateur, Tontine tontine) {
        Participant participant = new Participant();
        participant.setEmail(utilisateur.getEmail());
        participant.setNom(utilisateur.getNom());
        participant.setPrenom(utilisateur.getPrenom());
        participant.setDateNaiss(utilisateur.getDateNaiss());
        participant.setAdresse(utilisateur.getAdresse());
        participant.setNumTel(utilisateur.getNumTel());
        participant.setProfession(utilisateur.getProfession());
        participant.setSexe(utilisateur.getSexe());
        participant.setTontine(tontine);

        participantService.enregistrerPart(participant); // Save the participant

        return participant;
    }


    @Transactional
    public void refuserDemande(Demandes demande ){
        Tontine tontine = rechercheTontine(demande.getTontine().getId());

        if (tontine != null) {
            // Retirer l'utilisateur comme participant de la tontine s'il existe
            tontine.getParticipations().forEach(participation ->
                    participation.getParticipants().removeIf(participant ->
                            demande.getEmail().equals(participant.getEmail())));
        } else {
            throw new TontineNotFoundException("Tontine d'identifiant " + demande.getTontine().getId() + " non trouvée !");
        }
    }

    private int determineNombreRoles(Double montant) {

        return (int) (montant / 1000);
    }

}
