package com.andy.p1simple;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Using text blocks for HTML content
        String htmlResponse = """
                <html>
                <head>
                    <title>Hello Servlet</title>
                </head>
                <body>
                    <h1>Hello, World!</h1>
                    <p>This is a simple example of a servlet.</p>
                </body>
                </html>
                """;

        out.println(htmlResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Handle POST request here
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        // Handle PUT request here
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        // Handle DELETE request here
    }
}

