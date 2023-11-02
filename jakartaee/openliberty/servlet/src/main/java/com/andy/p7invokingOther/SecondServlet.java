package com.andy.p7invokingOther;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/second")
public class SecondServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Forward the request to ThirdServlet
        RequestDispatcher forwardDispatcher = request.getRequestDispatcher("/third");
        forwardDispatcher.forward(request, response);

        // This will not be shown
        response.getWriter().write("This is the end of SecondServer");

    }
}
