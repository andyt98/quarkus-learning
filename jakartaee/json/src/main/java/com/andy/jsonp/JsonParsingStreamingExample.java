package com.andy.jsonp;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;

import java.io.StringReader;

public class JsonParsingStreamingExample {
    public static void main(String[] args) {
        String jsonData = """
            {
                "firstName": "Duke",
                "lastName": "Java",
                "age": 18,
                "streetAddress": "100 Internet Dr",
                "city": "JavaTown",
                "state": "JA",
                "postalCode": "12345",
                "phoneNumbers": [
                    {"type": "mobile", "number": "111-111-1111"},
                    {"type": "home", "number": "222-222-2222"}
                ]
            }
            """;

        // Create a JsonParser instance
        JsonParser parser = Json.createParser(new StringReader(jsonData));

        // Iterate over parser events and process them
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();

            switch (event) {
                case START_ARRAY, END_ARRAY, START_OBJECT, END_OBJECT, VALUE_FALSE, VALUE_NULL, VALUE_TRUE ->
                        System.out.println(event);
                case KEY_NAME -> System.out.print(event + " " + parser.getString() + " - ");
                case VALUE_STRING, VALUE_NUMBER -> System.out.println(event + " " + parser.getString());
            }
        }

        // Close the parser
        parser.close();
    }
}
