package com.kosher.iskosher.exception;

import com.google.firebase.auth.FirebaseAuthException;
import com.kosher.iskosher.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // TODO: 2/11/2025 Add this MaxUploadSizeExceededException to glogal 
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request) {
        log.error("{} not found: {} {} {}",
                ex.getEntityType(), ex.getFieldName(), ex.getFieldValue(), ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Entity Not Found")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Resource Not Found")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessCreationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleBusinessCreationException(BusinessCreationException ex,
                                                                         HttpServletRequest request) {
        log.error("Business creation error: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Business Creation Error")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        log.error("Request body is missing or invalid: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .message("Request body is missing or invalid")
                .error("Bad Request")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                          HttpServletRequest request) {
        log.error("Method argument type mismatch: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Invalid parameter: " + ex.getName() + ". Expected type: " + ex.getRequiredType().getSimpleName())
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        log.error("Authentication error: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Authentication failed")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleServletException(
            ServletException ex,
            HttpServletRequest request) {
        log.error("Servlet exception: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .message("Servlet error: " + ex.getMessage())
                .error("Bad Request")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FirebaseAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleFirebaseAuthException(
            FirebaseAuthException ex,
            HttpServletRequest request) {
        log.error("Firebase authentication error: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Authentication failed")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ErrorResponse> handleJwtValidationException(JwtValidationException ex,
                                                                      HttpServletRequest request) {
        log.error("JWT validation error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("JWT Validation Error", ex.getMessage(), request.getRequestURI(),
                        Instant.now()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HandlerMethodValidationException ex,
                                                                    HttpServletRequest request) {
        String errorMessage = ex.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errorMessage)
                .error("Validation failed")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation failed")
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Illegal Argument")
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        log.debug("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DatabaseAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleDatabaseAccessException(
            DatabaseAccessException ex,
            HttpServletRequest request) {
        log.error("Database access error: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("Database Error")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessFilterException.class)
    public ResponseEntity<ErrorResponse> handleBusinessFilterException(
            BusinessFilterException ex, HttpServletRequest request) {

        log.error("Business search error: {}", ex.getMessage(), ex);

        HttpStatus status;
        String errorType;

        switch (ex.getErrorType()) {
            case DATABASE_ERROR:
                status = HttpStatus.SERVICE_UNAVAILABLE;
                errorType = "Database Error";
                break;
            case INVALID_FILTER:
                status = HttpStatus.BAD_REQUEST;
                errorType = "Invalid Filter Criteria";
                break;
            case NO_RESULTS:
                status = HttpStatus.NOT_FOUND;
                errorType = "No Results";
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorType = "Unexpected Error";
        }

        ErrorResponse error = ErrorResponse.builder()
                .message("An error occurred: " + ex.getMessage())
                .error(errorType)
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation error: {}", message);

        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .error("Validation Failed")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception ex,
            HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);

        ErrorResponse error = ErrorResponse.builder()
                .message("An unexpected error occurred")
                .error("Internal Server Error")
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
