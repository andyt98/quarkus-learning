package com.andy.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.List;

@ConfigMapping(prefix = "example")
public interface ExampleConfig {

    String greeting();

    int integer();

    @WithName("boolean")
    boolean boolProperty();

    List<String> countries();

    Origin origin();

    interface Origin {
        String name();
        String continent();
    }

}
