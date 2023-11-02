package com.andy.jsonp;

import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

public class JsonNavigationExample {
    public static void main(String[] args) {
        // Example JSON object model from Creating an Object Model from Application Code
        JsonObject model = JsonCreationExample.createJson();

        // Start navigation from the root of the JSON object model
        navigateTree(model, null);
    }



    public static void navigateTree(JsonValue tree, String key) {
        if (key != null)
            System.out.print("Key " + key + ": ");

        switch (tree.getValueType()) {
            case OBJECT -> {
                System.out.println("OBJECT");
                JsonObject object = (JsonObject) tree;
                for (String name : object.keySet())
                    navigateTree(object.get(name), name);
            }
            case ARRAY -> {
                System.out.println("ARRAY");
                JsonArray array = (JsonArray) tree;
                for (JsonValue val : array)
                    navigateTree(val, null);
            }
            case STRING -> {
                JsonString st = (JsonString) tree;
                System.out.println("STRING " + st.getString());
            }
            case NUMBER -> {
                JsonNumber num = (JsonNumber) tree;
                System.out.println("NUMBER " + num);
            }
            case TRUE, FALSE, NULL -> System.out.println(tree.getValueType().toString());
        }
    }
}
