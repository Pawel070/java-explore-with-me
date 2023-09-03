package ru.practicum.exceptions;

public class WrongEventDateException extends RuntimeException {

    public WrongEventDateException(final String message) {
        super(message);
    }
}