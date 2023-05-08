package com.nuuly.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends ResponseStatusException{

    public InvalidParameterException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
