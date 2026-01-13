package org.example.exception;
// липсващи обекти
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, long id) {
        super(resource + " with ID " + id + " not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
