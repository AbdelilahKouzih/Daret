package com.ouatson.backtontine.Participation;

import com.ouatson.backtontine.Participants.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipationService {
    @Autowired
    private ParticipationRepository participationRepository;

    public Participation enregistrerParticipation(Participation participation) {
        return participationRepository.save(participation);
    }

    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }


    public Participant getParticipantByEmail(Long participationId, String participantEmail) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new RuntimeException("Participation not found"));

        return participation.getParticipants().stream()
                .filter(participant -> participant.getEmail().equals(participantEmail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Participant not found in participation"));
    }



    public List<Participation> getParticipationsByParticipant(Long participantId) {
        // Utilisez le repository pour récupérer les participations par participant
        return participationRepository.findByParticipantsId(participantId);
    }
    public List<Participation> getParticipationsByTontine(Long tontineId) {
        // Utilisez le repository pour récupérer les participations par tontine
        return participationRepository.findByTontineId(tontineId);
    }
}
