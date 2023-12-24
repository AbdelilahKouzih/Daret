package com.ouatson.backtontine.Tontine;
import com.ouatson.backtontine.Demandes.Demandes;
import com.ouatson.backtontine.Participants.Participant;
import com.ouatson.backtontine.Participants.ParticipantService;
import com.ouatson.backtontine.Participation.Participation;
import com.ouatson.backtontine.Participation.ParticipationService;
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
    ParticipationService participationService;
    public List<Tontine> toutesMesTontines(User user){
        List<Tontine> all = tontineRepository.findAll();
        List<Tontine> mine = new ArrayList<>();

        all.forEach(tontine -> {
            Collection<Participation> participations = tontine.getParticipations();

            participations.forEach(participation -> {
                List<Participant> participants = participation.getParticipants();

                participants.forEach(participant -> {
                    if (user.getEmail().equals(participant.getEmail())) {
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
public void accepterDemande(Demandes demande) {
    Tontine tontine = rechercheTontine(demande.getTontine().getId());
    if (tontine != null) {
        List<User> utilisateurs = demande.getUtilisateurs();

        // Vérifier si les utilisateurs ne sont pas déjà participants
        for (User utilisateur : utilisateurs) {
            boolean isUserParticipant = tontine.getParticipations().stream()
                    .anyMatch(participation -> participation.getParticipants().stream()
                            .anyMatch(participant -> utilisateur.getEmail().equals(participant.getEmail())));

            if (!isUserParticipant) {
                // Ajouter l'utilisateur comme participant à la tontine
                Participant participant = new Participant();
                participant.setEmail(utilisateur.getEmail());
                participant.setTontine(tontine);
                participantService.enregistrerPart(participant);

                // Créer une participation pour l'utilisateur
                Participation participation = new Participation();
                participation.setTontine(tontine);
                participation.getParticipants().add(participant);

                // Enregistrer la participation
                participationService.enregistrerParticipation(participation);
            }
        }
    } else {
        throw new TontineNotFoundException("Tontine d'identifiant " + demande.getTontine().getId() + " non trouvée !");
    }
}


    @Transactional
    public void refuserDemande(Demandes demande ){
        Tontine tontine = rechercheTontine(demande.getTontine().getId());

        if (tontine != null) {
            // Retirer l'utilisateur comme participant de la tontine s'il existe
            tontine.getParticipations().forEach(participation ->
                    participation.getParticipants().removeIf(participant ->
                            demande.getEmail().equals(participant.getEmail())));

            // Ajouter d'autres logiques nécessaires pour refuser l'utilisateur
        } else {
            throw new TontineNotFoundException("Tontine d'identifiant " + demande.getTontine().getId() + " non trouvée !");
        }
    }


}
