package com.andy.alternatives;

import com.andy.alternatives.Greeting;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
public class AlternativeGreeting implements Greeting {
    @Override
    public String greet() {
        return "Hey! It's great to see you!";
    }
}
