package com.ouatson.backtontine.Participation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class ParticipationNotFoundException extends RuntimeException{
    public ParticipationNotFoundException(String message) {
        super(message);
    }
}
