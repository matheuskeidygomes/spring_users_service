package com.example.demo.rest.common;

import com.fasterxml.jackson.annotation.JsonInclude;
// import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ErrorMessage {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL) // Não inclui campos nulos na resposta de errors
    private Map<String, String> errors;

    public ErrorMessage(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public ErrorMessage(HttpStatus status, String message, BindingResult result) {
        this.status = status.value();
        this.message = message;
        addErrors(result);
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
    }
}
