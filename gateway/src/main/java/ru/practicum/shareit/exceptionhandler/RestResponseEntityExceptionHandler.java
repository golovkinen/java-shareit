package ru.practicum.shareit.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WrongStateException.class})
    public ResponseEntity<Object> handleWrongStateException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
