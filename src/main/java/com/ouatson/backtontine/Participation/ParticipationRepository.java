package com.ouatson.backtontine.Participation;


import com.ouatson.backtontine.Participants.Participant;
import com.ouatson.backtontine.Tontine.Tontine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByTontine(Tontine tontine);

    List<Participation> findByParticipantsContaining(Participant participant);

    List<Participation> findByTontineId(Long tontineId);

    List<Participation> findByParticipantsId(Long participantId);
// Ajoutez d'autres méthodes au besoin

}