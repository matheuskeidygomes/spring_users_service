package com.example.demo.rest.helpers;

// import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.rest.exceptions.BadRequestException;
import com.example.demo.rest.common.ErrorMessage;
import com.example.demo.rest.exceptions.NotFoundUserException;
import com.example.demo.rest.exceptions.UniqueUserEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice           // Indica que essa classe será usada para tratar exceções lançadas pelos controllers
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)    // Indica que esse método será chamado quando uma exceção do tipo MethodArgumentNotValidException for lançada pelo controller
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, BindingResult result) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid arguments", result));
    }

    @ExceptionHandler(UniqueUserEmailException.class)
    public ResponseEntity<ErrorMessage> handleUniqueUserEmailException(UniqueUserEmailException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundUserException(NotFoundUserException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        System.out.println(e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }
}
