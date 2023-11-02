package com.andy.events.sync;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;
import jakarta.enterprise.event.TransactionPhase;

import java.util.logging.Logger;

@ApplicationScoped
public class EmailNotificationService {
    private static final Logger logger = Logger.getLogger(EmailNotificationService.class.getName());

    public void sendEmailNotification(@Observes Order order) {
        // Send email notification to the customer about the placed order
        logger.info(String.format("Sending email notification for order placed at: %s of amount %s",
                order.getPlacedAt(), order.getAmount()));
    }

    public void sendHighAmountEmailNotification(@Observes(
            notifyObserver = Reception.IF_EXISTS,
            during = TransactionPhase.AFTER_COMPLETION)
                                                Order order) {

        // Check if the order amount exceeds a threshold
        double threshold = 100.0; // Threshold amount for conditional processing
        if (order.getAmount() > threshold) {
            // Send email notification to the customer about the placed order
            logger.info(String.format("Sending conditional email notification for order placed at: %s of amount %s",
                    order.getPlacedAt(), order.getAmount()));
        }
    }
}
