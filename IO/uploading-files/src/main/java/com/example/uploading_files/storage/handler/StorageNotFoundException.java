package com.example.uploading_files.storage.handler;

public class StorageNotFoundException extends StorageException {

    public StorageNotFoundException(String message) {
        super(message);
    }

    public StorageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
