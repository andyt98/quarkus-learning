package com.andy.jsonb;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class JsonDeserializationExample {
    public static void main(String[] args) throws Exception {
        // Create a JSON-B instance
        try (Jsonb jsonb = JsonbBuilder.create()) {
            // JSON data to be deserialized
            String jsonData = """
                    {
                        "first-name": "Alice",
                        "last-name": "Smith",
                        "age": 25
                    }
                    """;

            // Deserialize JSON data into a Java object
            Person person = jsonb.fromJson(jsonData, Person.class);

            System.out.println("First Name: " + person.getFirstName());
            System.out.println("Last Name: " + person.getLastName());
            System.out.println("Age: " + person.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
