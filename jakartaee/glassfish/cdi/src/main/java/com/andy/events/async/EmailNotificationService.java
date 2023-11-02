package com.andy.events.async;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailNotificationService {
    private static final Logger logger = Logger.getLogger(EmailNotificationService.class.getName());

    public void sendEmailNotification(@ObservesAsync Order order) {
        logger.info(String.format("Received order notification. Order placed at: %s, Amount: %s...",
                order.getPlacedAt(), order.getAmount()));

        // Simulate a long-running operation
        logger.info("Simulating a long-running operation...");
        try {
            TimeUnit.SECONDS.sleep(5); // Simulating a 5-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Send email notification to the customer about the placed order
        logger.info(String.format("Sending email notification for order placed at: %s of amount %s....",
                order.getPlacedAt(), order.getAmount()));
    }
}
