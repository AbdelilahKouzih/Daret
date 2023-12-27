package com.ouatson.backtontine.Participation;

import com.ouatson.backtontine.Participants.Participant;
import com.ouatson.backtontine.Tontine.Tontine;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "participation_participant",
            joinColumns = @JoinColumn(name = "participation_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    @ManyToOne
    private Tontine tontine;

    public Participation() {
        this.participants = new ArrayList<>();
    }

    public Tontine getTontine() {
        return tontine;
    }

    public void setTontine(Tontine tontine) {
        this.tontine = tontine;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
