package com.andy.p12async;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/asyncio"}, asyncSupported = true)
public class AsyncIOServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AsyncIOServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final AsyncContext asyncContext = request.startAsync();
        final ServletInputStream input = request.getInputStream();

        input.setReadListener(new ReadListener() {
            final byte[] buffer = new byte[4 * 1024];
            final StringBuilder sbuilder = new StringBuilder();

            @Override
            public void onDataAvailable() {
                try {
                    do {
                        int length = input.read(buffer);
                        if (length > 0) {
                            sbuilder.append(new String(buffer, 0, length));
                        }
                    } while (input.isReady());
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error reading input data: {0}", ex.getMessage());
                }
            }

            @Override
            public void onAllDataRead() {
                try {
                    // Process the received data
                    String requestData = sbuilder.toString();
                    LOGGER.info("Received data: " + requestData);

                    // Simulate some processing delay
                    Thread.sleep(2000);

                    // Write response to the client
                    String responseContent = "Processed data: " + requestData;
                    asyncContext.getResponse().getWriter().write(responseContent);
                } catch (IOException | InterruptedException ex) {
                    LOGGER.log(Level.SEVERE, "Error processing request: {0}", ex.getMessage());
                }

                // Complete the asynchronous processing
                asyncContext.complete();
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.log(Level.SEVERE, "Error in nonblocking I/O: {0}", t.getMessage());
            }
        });
    }
}
