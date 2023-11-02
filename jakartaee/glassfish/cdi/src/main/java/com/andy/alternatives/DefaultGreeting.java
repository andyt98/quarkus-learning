package com.andy.alternatives;

import jakarta.enterprise.context.Dependent;

@Dependent
public class DefaultGreeting implements Greeting {
    @Override
    public String greet() {
        return "Hello!";
    }
}
