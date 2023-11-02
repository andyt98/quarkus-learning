package com.andy.specializations;

import jakarta.enterprise.context.Dependent;

@Dependent
public class DefaultProduct implements Product {
    @Override
    public String getName() {
        return "Default Product";
    }
}
