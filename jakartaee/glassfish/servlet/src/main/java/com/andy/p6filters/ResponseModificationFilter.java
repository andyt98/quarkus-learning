package com.andy.p6filters;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/hello")
public class ResponseModificationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Continue the filter chain
        chain.doFilter(request, response);

        // Modify the response content by appending a footer
        String originalResponse = response.toString();
        String modifiedResponse = originalResponse + "\n\n--- This is the modified response footer ---";

        // Write the modified response content back to the response
        PrintWriter out = response.getWriter();
        out.write(modifiedResponse);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
