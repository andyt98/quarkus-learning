package com.andy.p7invokingOther;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Code to process request and generate content

        // Include the output of SecondServlet
        RequestDispatcher dispatcher = request.getRequestDispatcher("/third");
        dispatcher.include(request, response);

        // Continue generating the rest of the content
        response.getWriter().write("This is the end of FirstServlet");
    }
}
