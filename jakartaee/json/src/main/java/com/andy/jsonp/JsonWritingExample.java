package com.andy.jsonp;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;

import java.io.StringWriter;


public class JsonWritingExample {
    public static void main(String[] args) {
        // Example JSON object model from Creating an Object Model from Application Code
        JsonObject model = JsonCreationExample.createJson();

        // Write the JSON object model to a stream
        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(model);
        }

        // Get the JSON data as a string and print it
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
    }
}
