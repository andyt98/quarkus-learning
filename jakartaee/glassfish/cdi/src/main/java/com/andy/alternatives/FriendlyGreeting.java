package com.andy.alternatives;

import jakarta.enterprise.context.Dependent;

@Dependent
public class FriendlyGreeting implements Greeting {
    @Override
    public String greet() {
        return "Hi there! How are you?";
    }
}
