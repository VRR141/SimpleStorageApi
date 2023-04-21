package org.vrr.simplecloudservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vrr.simplecloudservice.dto.response.CustomExceptionResponse;
import org.vrr.simplecloudservice.excecption.ClientNotFoundByEmailException;
import org.vrr.simplecloudservice.excecption.EmailAlreadyExistException;
import org.vrr.simplecloudservice.excecption.InvalidFileIdentifierException;

@RestControllerAdvice
@Slf4j
public class DefaultRestExceptionHandler {

    private static final String THROWABLE_MESSAGE = "Something went wrong. Try again later";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handle(exception));
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<CustomExceptionResponse> handleEmailAlreadyExistException(EmailAlreadyExistException exception){
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(handle(exception));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomExceptionResponse> handleBadCredentialsException(BadCredentialsException e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handle(e));
    }

    @ExceptionHandler(ClientNotFoundByEmailException.class)
    public ResponseEntity<CustomExceptionResponse> handleClientNotFoundByEmailException(ClientNotFoundByEmailException e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handle(e));
    }

    @ExceptionHandler(InvalidFileIdentifierException.class)
    public ResponseEntity<CustomExceptionResponse> handleInvalidFileIdentifierException(InvalidFileIdentifierException e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handle(e));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CustomExceptionResponse> handleThrowable(Throwable throwable){
        log.error(throwable.getMessage(), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomExceptionResponse(THROWABLE_MESSAGE));
    }

    private CustomExceptionResponse handle(Exception e){
        return new CustomExceptionResponse(e.getMessage());
    }
}
