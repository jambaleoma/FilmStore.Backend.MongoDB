package it.enzo.me.FilmStore.backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse errorDetails =
                new ExceptionResponse(sdf.format(new Timestamp(System.currentTimeMillis())), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleGemmaExceptions(BadRequestException ex, WebRequest request) {
        ExceptionResponse errorDetails =
                new ExceptionResponse(sdf.format(new Timestamp(System.currentTimeMillis())), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleGemmeExceptions(NotFoundException ex, WebRequest request) {
        ExceptionResponse errorDetails =
                new ExceptionResponse(sdf.format(new Timestamp(System.currentTimeMillis())), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotCreatedException.class)
    public final ResponseEntity<Object> handleGemmeExceptions(NotCreatedException ex, WebRequest request) {
        ExceptionResponse errorDetails =
                new ExceptionResponse(sdf.format(new Timestamp(System.currentTimeMillis())), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public final ResponseEntity<Object> handleGemmeExceptions(AlreadyExistException ex, WebRequest request) {
        ExceptionResponse errorDetails =
                new ExceptionResponse(sdf.format(new Timestamp(System.currentTimeMillis())), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.CONFLICT);
    }
}
