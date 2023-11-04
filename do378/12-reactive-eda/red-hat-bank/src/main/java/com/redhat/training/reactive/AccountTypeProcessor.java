package com.redhat.training.reactive;

import com.redhat.training.event.BankAccountWasCreated;
import com.redhat.training.model.BankAccount;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;


@ApplicationScoped
public class AccountTypeProcessor {
    private static final Logger LOGGER = Logger.getLogger(AccountTypeProcessor.class);

    @Inject
    Mutiny.SessionFactory sessionFactory; // Inject the SessionFactory

    @Incoming("new-bank-accounts-in")
    @ActivateRequestContext
    public Uni<Void> processNewBankAccountEvents(BankAccountWasCreated event) {
        return sessionFactory.withTransaction((session, tx) -> {
            String assignedAccountType = calculateAccountType(event.balance);
            logEvent(event, assignedAccountType);

            // Chain the operations on the session here.
            return BankAccount.<BankAccount>findById(event.id)
                    .onItem().ifNotNull()
                    .invoke(entity -> entity.type = assignedAccountType)
                    .replaceWithVoid();
        });
    }

    public String calculateAccountType(Long balance) {
        return balance >= 100000 ? "premium" : "regular";
    }

    private void logEvent(BankAccountWasCreated event, String assignedType) {
        LOGGER.infov(
                "Processing BankAccountWasCreated - ID: {0} Balance: {1} Type: {2}",
                event.id,
                event.balance,
                assignedType
        );
    }
}
