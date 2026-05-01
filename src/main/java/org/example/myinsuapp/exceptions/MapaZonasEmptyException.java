package org.example.myinsuapp.exceptions;

public class MapaZonasEmptyException extends RuntimeException {

    public MapaZonasEmptyException(String message) {
        super(message);
    }

    public MapaZonasEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
