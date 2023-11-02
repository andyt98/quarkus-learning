package com.andy.vertx_learning.p1_basics;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/***
 * <p>The VertxEcho class implements a simple network service using Vert.x, a reactive, event-driven,
 * and non-blocking toolkit for building reactive applications on the Java Virtual Machine (JVM).
 * This class demonstrates the use of Vert.x to create a TCP echo server and an HTTP server
 * that reports the number of open connections.
 * <ul>
 *   <li>{@link #main(String[])} method initializes Vert.x, creates network servers, and handles periodic tasks.</li>
 *   <li>{@link #handleNewClient(NetSocket)} method handles new TCP client connections and echoes received data.</li>
 *   <li>{@link #howMany()} method returns the current number of open connections.</li>
 * </ul>
 *
 * <p>Vert.x uses an event loop to efficiently process events such as new connections, data arrival, and HTTP requests.
 * Callbacks are used to handle asynchronous events in a non-blocking manner.
 *
 */

public class VertxEcho {

    private static int numberOfConnections = 0;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.createNetServer()
                .connectHandler(VertxEcho::handleNewClient)
                .listen(3000);

        vertx.setPeriodic(5000, id -> System.out.println(howMany()));

        vertx.createHttpServer()
                .requestHandler(request -> request.response().end(howMany()))
                .listen(8080);
    }

    private static void handleNewClient(NetSocket socket) {
        numberOfConnections++;
        socket.handler(buffer -> {
            socket.write(buffer);
            if (buffer.toString().endsWith("/quit\n")) {
                socket.close();
            }
        });
        socket.closeHandler(v -> numberOfConnections--);
    }

    private static String howMany() {
        return "We now have " + numberOfConnections + " connections";
    }
}