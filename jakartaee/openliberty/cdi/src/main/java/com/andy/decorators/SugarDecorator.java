package com.andy.decorators;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Decorator
@Dependent
public class SugarDecorator implements Coffee {
    @Inject
    @Delegate
    private Coffee coffee;

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", with Sugar";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.3;
    }
}
