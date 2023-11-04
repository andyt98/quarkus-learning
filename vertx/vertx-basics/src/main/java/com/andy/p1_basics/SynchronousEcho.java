package com.andy.p1_basics;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This Java program demonstrates a simple TCP server that echoes input text back to the client until it receives a "/quit" command.
 * It uses a synchronous I/O model, allocating a new thread for each incoming connection.
 * This model, while simple, can lead to resource wastage and increased operating costs as it allocates threads for each connection.
 * In scenarios with many concurrent connections, this approach can quickly exhaust system resources.
 */

public class SynchronousEcho {

    public static void main(String[] args) throws Throwable {
        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(3000));
            while (true) {
                Socket socket = server.accept();
                new Thread(clientHandler(socket)).start();
            }
        }
    }

    private static Runnable clientHandler(Socket socket) {
        return () -> {
            try (
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(
                            new OutputStreamWriter(socket.getOutputStream()))) {
                String line = "";
                while (!"/quit".equals(line)) {
                    line = reader.readLine();      // <2>
                    System.out.println("~ " + line);
                    writer.write(line + "\n");  // <3>
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
