package com.andy.p3_eventbus.basics.customcodec;

public class Ping {

    private String message;
    private boolean enabled;

    public Ping() {
        // Default Constructor
    }

    public Ping(final String message, final boolean enabled) {
        this.message = message;
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "Ping{" +
                "message='" + message + '\'' +
                ", enabled=" + enabled +
                '}';
    }

}
