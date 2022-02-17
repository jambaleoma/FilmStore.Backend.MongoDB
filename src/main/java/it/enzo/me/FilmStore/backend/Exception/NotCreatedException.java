package it.enzo.me.FilmStore.backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotCreatedException extends RuntimeException {
    public NotCreatedException(String message) {
        super(message);
    }

    public NotCreatedException() {}
}