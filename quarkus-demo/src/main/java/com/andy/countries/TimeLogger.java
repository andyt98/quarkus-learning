package com.andy.countries;

import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

@ApplicationScoped
@Startup
public class TimeLogger {

    @Inject
    Logger logger;

    @Scheduled(every = "3s")
    public void logTime() {
        logger.log("Now it is " + Instant.now());
    }

}
