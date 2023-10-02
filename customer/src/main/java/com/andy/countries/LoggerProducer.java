package com.andy.countries;


import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;


public class LoggerProducer {

    @Produces
    @Dependent
    public Logger createLogger() {
        System.out.println("createLogger() invoked");
        return new Logger();
    }

}
