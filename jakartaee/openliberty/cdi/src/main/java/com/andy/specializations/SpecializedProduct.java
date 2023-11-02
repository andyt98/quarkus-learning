package com.andy.specializations;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Specializes;

@Dependent
@Specializes
public class SpecializedProduct extends DefaultProduct {
    @Override
    public String getName() {
        return "Specialized Product";
    }
}
