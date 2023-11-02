package com.andy.jsonp;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;

public class JsonParsingExample {
    public static void main(String[] args) {
        // JSON-formatted text block
        String jsonTextBlock = """
            {
                "name": "John Doe",
                "age": 30,
                "city": "New York"
            }
            """;

        try {
            // Create a JsonReader using a StringReader for the JSON text block
            try (JsonReader reader = Json.createReader(new StringReader(jsonTextBlock))) {
                // Read JSON data and create a JsonObject
                JsonObject jsonObject = reader.readObject();

                // Access and print values from the JsonObject
                String name = jsonObject.getString("name");
                int age = jsonObject.getInt("age");
                String city = jsonObject.getString("city");

                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("City: " + city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
