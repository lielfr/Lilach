package org.cshaifasweng.winter.events;

public class TokenSetEvent {
    public final String token;

    public TokenSetEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
