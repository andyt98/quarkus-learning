package com.andy.p12async;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AsyncIOClient {
    private final static String servletUrl = "http://localhost:8080/servlet/asyncio";
    private final static String postData = "Hello, Async!";

    public static void main(String[] args) {
        try {
            // Create URL object
            URL url = new URL(servletUrl);

            // Open connection
            HttpURLConnection connection = getHttpURLConnection(url);

            // Get the response code
            int responseCode = connection.getResponseCode();

            System.out.println("Sending POST request to: " + servletUrl);
            System.out.println("Response Code: " + responseCode);

            // Read and print the response
            readAndPrintResponse(connection);

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method
        connection.setRequestMethod("POST");

        // Enable input/output streams
        connection.setDoOutput(true);

        // Write data to the request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            os.write(postDataBytes);
        }
        return connection;
    }

    private static void readAndPrintResponse(HttpURLConnection connection) throws IOException {
        try (InputStream is = connection.getInputStream()) {
            byte[] responseBytes = is.readAllBytes();
            String responseContent = new String(responseBytes, StandardCharsets.UTF_8);
            System.out.println("Response Content -> " + responseContent);
        }
    }
}
