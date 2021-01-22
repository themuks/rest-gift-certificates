package com.epam.esm.controller.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> entityNotFoundException(EntityNotFoundException ex) {
        String errorCode = HttpStatus.NOT_FOUND.value() + ex.getEntityCode();
        return buildResponseEntity(new ApiError(ex.getLocalizedMessage(), errorCode), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex) {
        String errorCode = Integer.toString(HttpStatus.BAD_REQUEST.value());
        return buildResponseEntity(new ApiError(ex.getLocalizedMessage(), errorCode), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerInternalErrorException.class)
    protected ResponseEntity<Object> serverInternalErrorException(ServerInternalErrorException ex) {
        String errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value() + ex.getEntityCode();
        return buildResponseEntity(new ApiError(ex.getLocalizedMessage(), errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(ex.getParameterName() + " parameter is missing", Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(ex.getPropertyName() + " should be of type " + ex.getRequiredType(),
                Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError("Malformed JSON request", Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
