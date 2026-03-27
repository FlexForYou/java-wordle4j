package ru.yandex.practicum.exceptions;

public class InvalidWordLengthException extends RuntimeException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
}
