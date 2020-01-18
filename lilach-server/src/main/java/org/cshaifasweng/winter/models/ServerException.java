package org.cshaifasweng.winter.models;

// Note: This is NOT an entity!
public class ServerException {
    String message;

    public ServerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
