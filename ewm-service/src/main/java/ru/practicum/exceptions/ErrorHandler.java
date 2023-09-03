package ru.practicum.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Collections;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.practicum.MyConstants;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidException(NotValidException exception) {
        log.info("ErrorHandler handleNotValidException: BAD_REQUEST {}", exception);
        return ApiError.builder()
                .errors(Collections.singletonList(Arrays.toString(exception.getStackTrace())))
                .cause("bad request")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Throwable exception, HttpStatus status) {
        log.info("ErrorHandler handleException: INTERNAL_SERVER_ERROR {}", exception);
        StringWriter out = new StringWriter();
        exception.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return new ApiError(status, "Error", exception.getMessage(), Collections.singletonList(stackTrace),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException exception) {
        log.info("ErrorHandler handleNotFoundException: NOT_FOUND {}", exception);
        return ApiError.builder()
                .errors(Collections.singletonList(Arrays.toString(exception.getStackTrace())))
                .cause("Not found")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.info("ErrorHandler handleDataIntegrityViolationException: CONFLICT {}", exception);
        return ApiError.builder()
                .errors(Collections.singletonList(Arrays.toString(exception.getStackTrace())))
                .cause("conflict")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT.getReasonPhrase().toUpperCase())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleWrongEventDateException(WrongEventDateException exception) {
        log.info("ErrorHandler handleWrongEventDateException: FORBIDDEN {}", exception);
        return ApiError.builder()
                .errors(Collections.singletonList(Arrays.toString(exception.getStackTrace())))
                .cause("conflict")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT.getReasonPhrase().toUpperCase())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException exception) {
        log.info("ErrorHandler handleConstraintViolationException: BAD_REQUEST {}", exception);
        return ApiError.builder()
                .errors(Collections.singletonList(Arrays.toString(exception.getStackTrace())))
                .cause("Not valid")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.info("ErrorHandler handleMethodArgumentNotValid: BAD_REQUEST {}", exception);
        return ApiError.builder()
                .errors(Collections.singletonList(Arrays.toString(exception.getStackTrace())))
                .cause("bad request")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase())
                .build();
    }

}