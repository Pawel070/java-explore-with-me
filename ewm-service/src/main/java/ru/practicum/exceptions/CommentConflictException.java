package ru.practicum.exceptions;

public class CommentConflictException extends RuntimeException {
    public CommentConflictException(final String message) {
        super(message);
    }
}
