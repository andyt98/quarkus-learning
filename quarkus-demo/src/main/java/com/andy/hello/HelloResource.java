package com.andy.hello;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
@Path("hello")
public class HelloResource {

    @Inject
    ExampleConfig exampleConfig;

    @ConfigProperty(name = "example.greeting")
    String greeting;

    @Location("hello.txt")
    Template helloTemplate;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance hello() {
        useConfig();
        TestObject object = new TestObject();
        object.setName("Hello");

        return helloTemplate
                .data("greeting", "Hello")
                .data("items", List.of("Hello", "World"))
                .data("object", object);
    }

    @TemplateExtension
    static String formattedName(TestObject object) {
        return "//" + object.name + "//";
    }

    public static class TestObject {

        private String name;

        public String getName() {
            System.out.println("TestObject.getName invoked");
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private void useConfig(){
        System.out.println("greeting = " + greeting);
        System.out.println("exampleConfig.greeting() = " + exampleConfig.greeting());
        System.out.println("exampleConfig.integer() = " + exampleConfig.integer());
        System.out.println("exampleConfig.boolProperty() = " + exampleConfig.boolProperty());
        System.out.println("exampleConfig.origin().name() = " + exampleConfig.origin().name());
        System.out.println("coffeeConfig.origin.continent = " + exampleConfig.origin().continent());
        System.out.println("exampleConfig.countries() = " + exampleConfig.countries());
    }

}
