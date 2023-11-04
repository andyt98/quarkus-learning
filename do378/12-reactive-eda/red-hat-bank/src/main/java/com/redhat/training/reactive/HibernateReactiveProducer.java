package com.redhat.training.reactive;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public class HibernateReactiveProducer {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Produces
    public Uni<Mutiny.Session> produceSession() {
        return sessionFactory.withSession(session -> Uni.createFrom().item(session));
    }
}
