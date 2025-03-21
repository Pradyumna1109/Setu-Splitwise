package com.example.setu_spliwise.common;

import com.example.setu_spliwise.exceptions.*;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

  // Handle BadRequestException
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Handle ConflictException
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Object> handleConflictException(ConflictException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
  }

  // Handle NotFoundException
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  // Handle UnAuthorisedException
  @ExceptionHandler(UnAuthorisedException.class)
  public ResponseEntity<Object> handleUnAuthorisedException(UnAuthorisedException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  // Handle Jakarta Validation Errors (MethodArgumentNotValidException)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    error -> error.getField(),
                    error -> error.getDefaultMessage(),
                    (existing, replacement) -> existing // Handle duplicates
                    ));

    return buildErrorResponse("Validation Failed", HttpStatus.BAD_REQUEST, errors);
  }

  // Handle ConstraintViolationException (e.g., @Valid on PathVariables or RequestParams)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> errors =
        ex.getConstraintViolations().stream()
            .collect(
                Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) -> existing));

    return buildErrorResponse("Validation Failed", HttpStatus.BAD_REQUEST, errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleInvalidJsonException(HttpMessageNotReadableException ex) {
    return buildErrorResponse("Invalid request body: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Handle missing request parameters
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handleMissingParameterException(
      MissingServletRequestParameterException ex) {
    return buildErrorResponse(
        "Missing request parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
  }

  // Handle missing path variables
  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<Object> handleMissingPathVariableException(
      MissingPathVariableException ex) {
    return buildErrorResponse(
        "Missing path variable: " + ex.getVariableName(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InputCoercionException.class)
  public ResponseEntity<String> handleInputCoercionException(InputCoercionException ex) {
    return new ResponseEntity<>(
        "Invalid input data: " + ex.getOriginalMessage(), HttpStatus.BAD_REQUEST);
  }

  // Generic Exception Handler
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) {
    log.error(ex.getMessage(), ex);
    return buildErrorResponse(
        "An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Helper Method to Build Error Response
  private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
    return buildErrorResponse(message, status, null);
  }

  private ResponseEntity<Object> buildErrorResponse(
      String message, HttpStatus status, Map<String, String> errors) {
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("timestamp", LocalDateTime.now());
    errorDetails.put("status", status.value());
    errorDetails.put("error", status.getReasonPhrase());
    errorDetails.put("message", message);

    if (errors != null && !errors.isEmpty()) {
      errorDetails.put("errors", errors);
    }

    log.error(message);
    return new ResponseEntity<>(errorDetails, status);
  }
}
