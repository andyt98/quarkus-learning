package com.andy.countries;


import jakarta.annotation.PostConstruct;

public class Logger {

    @PostConstruct
    void init() {
        System.out.println("logger created");
    }

    public void log(String message) {
        System.out.println(message);
    }

}
