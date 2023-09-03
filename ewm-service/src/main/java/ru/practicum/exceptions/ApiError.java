package ru.practicum.exceptions;

import java.util.List;

import lombok.*;

import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ApiError {

    List<String> errors;
    String message;
    String cause;
    String status;
    String timestamp;

    public <T> ApiError(HttpStatus status, String cause, String message, List<T> errors, String timestamp) {
    }

}