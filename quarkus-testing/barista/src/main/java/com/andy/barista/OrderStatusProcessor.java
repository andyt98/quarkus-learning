package com.andy.barista;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderStatusProcessor {

    public String process(final String status) {
        return switch (status) {
            case "PREPARING" -> "FINISHED";
            case "FINISHED", "COLLECTED" -> "COLLECTED";
            default -> throw new IllegalArgumentException("Unknown status " + status);
        };
    }

}
