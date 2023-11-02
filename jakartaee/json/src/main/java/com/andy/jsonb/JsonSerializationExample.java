package com.andy.jsonb;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

public class JsonSerializationExample {
    public static void main(String[] args) {
        // Create a Java object
        Person person = new Person("John", "Doe", 30);

        JsonbConfig config = new JsonbConfig().withFormatting(true);

        // Serialize the Java object to JSON within a try-with-resources block
        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            String json = jsonb.toJson(person);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
