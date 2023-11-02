package com.andy.jsonp;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JsonCreationExample {
    public static void main(String[] args) {
        // Create an object model representing a person's information
        JsonObject model = createJson();

        // Print the JSON object as a string
        System.out.println(model.toString());
    }

    static JsonObject createJson() {
        return Json.createObjectBuilder()
                .add("firstName", "Duke")
                .add("lastName", "Java")
                .add("age", 18)
                .add("streetAddress", "100 Internet Dr")
                .add("city", "JavaTown")
                .add("state", "JA")
                .add("postalCode", "12345")
                .add("phoneNumbers", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("type", "mobile")
                                .add("number", "111-111-1111"))
                        .add(Json.createObjectBuilder()
                                .add("type", "home")
                                .add("number", "222-222-2222")))
                .build();
    }
}
