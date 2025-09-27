package ch.hambak.lamp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public void handleException(Exception e) {
        log.error("Unhandled exception", e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public void handleNoHandlerFoundException(NoResourceFoundException e) {
        log.error("Invalid URL: {}", e.getMessage());

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public void handleNoSuchElementException(NoSuchElementException e) {
        log.warn("Resource not found: {}", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleJsonBindingError(HttpMessageNotReadableException e) {
        log.error("Invalid json body: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid request body");
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleBeanValidation(MethodArgumentNotValidException e) {
        log.error("Invalid request body: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            // FieldError인 경우에만 필드명을 키로 사용
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            // ObjectError인 경우 객체명을 키로 사용할 수 있음
            else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public void handleIllegalStateException(IllegalStateException e) {
        log.warn("Illegal state: {}", e.getMessage());
    }
}
