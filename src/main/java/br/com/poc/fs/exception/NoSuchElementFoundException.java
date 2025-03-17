package br.com.poc.fs.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class NoSuchElementFoundException extends ResponseStatusException {

    public NoSuchElementFoundException() {
        super(NOT_FOUND);
    }

    public NoSuchElementFoundException(String errorMessage) {
        super(NOT_FOUND, errorMessage);
    }

}
