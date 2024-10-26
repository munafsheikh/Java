package com.virtrics.experiment.exception;

public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(Object o) {
        super("Object not found: " + o.toString());
    }

    public ObjectNotFoundException(Long id) {
        super("Object not found: " + id.toString());
    }
}
