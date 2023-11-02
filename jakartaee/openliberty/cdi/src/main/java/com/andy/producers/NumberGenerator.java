package com.andy.producers;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class NumberGenerator {

    // method producer
    @Produces
    @RandomNumber
    public int generateRandomNumber() {
        return (int) (Math.random() * 100);
    }

    public void close(@Disposes @RandomNumber int random) {
        System.out.println("RandomNumber Disposer Method for number " + random);
    }

    @PostConstruct
    public void init() {
        System.out.println("NumberGenerator initialized");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("NumberGenerator cleaned up");
    }
}