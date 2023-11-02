package com.andy.jsonp;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;

import java.io.StringWriter;

public class JsonWritingStreamingExample  {
    public static void main(String[] args) {
        StringWriter stringWriter = new StringWriter();

        // Create a JsonGenerator instance
        JsonGenerator gen = Json.createGenerator(stringWriter);

        // Write JSON data to the string
        gen.writeStartObject()
           .write("firstName", "Duke")
           .write("lastName", "Java")
           .write("age", 18)
           .write("streetAddress", "100 Internet Dr")
           .write("city", "JavaTown")
           .write("state", "JA")
           .write("postalCode", "12345")
           .writeStartArray("phoneNumbers")
               .writeStartObject()
                   .write("type", "mobile")
                   .write("number", "111-111-1111")
               .writeEnd()
               .writeStartObject()
                   .write("type", "home")
                   .write("number", "222-222-2222")
               .writeEnd()
           .writeEnd()
        .writeEnd();

        // Close the JsonGenerator (also closes the StringWriter)
        gen.close();

        String jsonData = stringWriter.toString();
        System.out.println(jsonData);
    }
}
