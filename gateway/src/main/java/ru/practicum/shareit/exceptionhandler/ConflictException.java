package ru.practicum.shareit.exceptionhandler;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
