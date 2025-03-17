package br.com.poc.fs.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class BusinessException extends ResponseStatusException {

    public BusinessException() {
        super(BAD_REQUEST);
    }

    public BusinessException(String errorMessage) {
        super(BAD_REQUEST, errorMessage);
    }

}
