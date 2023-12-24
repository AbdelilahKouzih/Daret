package com.ouatson.backtontine.Participation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participations")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @PostMapping
    public Participation ajouterParticipation(@RequestBody Participation participation) {
        return participationService.enregistrerParticipation(participation);
    }

    @GetMapping
    public List<Participation> getAllParticipations() {
        return participationService.getAllParticipations();
    }

    @GetMapping("/tontine/{tontineId}")
    public List<Participation> getParticipationsByTontine(@PathVariable Long tontineId) {
        // Utilisez le service pour récupérer les participations par tontine
        List<Participation> participations = participationService.getParticipationsByTontine(tontineId);

        // Vous pouvez effectuer d'autres traitements si nécessaire

        // Retournez la liste des participations
        return participations;
    }

    @GetMapping("/participant/{participantId}")
    public List<Participation> getParticipationsByParticipant(@PathVariable Long participantId) {
        // Utilisez le service pour récupérer les participations par participant
        List<Participation> participations = participationService.getParticipationsByParticipant(participantId);

        // Vous pouvez effectuer d'autres traitements si nécessaire

        // Retournez la liste des participations
        return participations;
    }


    // Ajoutez d'autres méthodes au besoin
}
