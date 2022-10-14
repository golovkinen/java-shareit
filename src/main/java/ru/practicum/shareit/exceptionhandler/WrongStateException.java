package ru.practicum.shareit.exceptionhandler;

public class WrongStateException extends RuntimeException {
    public WrongStateException(String message) {
        super(message);
    }
}
