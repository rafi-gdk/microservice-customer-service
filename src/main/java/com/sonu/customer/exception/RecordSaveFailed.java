package com.sonu.customer.exception;

public class RecordSaveFailed extends RuntimeException {
    public RecordSaveFailed(String message) {
        super(message);
    }
}
