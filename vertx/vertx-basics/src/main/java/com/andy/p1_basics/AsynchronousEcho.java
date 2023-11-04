package com.andy.p1_basics;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * This class demonstrates asynchronous programming with non-blocking I/O using Java NIO.
 * Instead of waiting for I/O operations to complete, it leverages non-blocking I/O to
 * efficiently handle multiple concurrent connections on a single thread.
 * The main loop in this class uses a Java NIO Selector to monitor events on socket channels,
 * such as accepting new connections, reading data from clients, and writing data back to clients.
 *
 * <p>The code is divided into three main methods:
 * <ul>
 * <li>{@link #newConnection(Selector, SelectionKey)} handles accepting new client connections. </li>
 * <li>{@link #echo(SelectionKey)} handles reading data from and writing data back to clients. </li>
 * <li>{@link #continueEcho(Selector, SelectionKey)} continues writing data back to clients when necessary. </li>
 * </ul>
 * <p>This asynchronous approach allows the server to efficiently manage multiple connections
 * without blocking on I/O operations. However, it also increases code complexity compared to
 * using blocking I/O.
 *
 * <p>It's important to note that Java NIO provides non-blocking I/O APIs but does not handle
 * higher-level protocol-specific tasks or specify a threading model. Developers often use
 * networking libraries like Netty and Apache MINA to simplify network programming.
 */
public class AsynchronousEcho {

    private static final HashMap<SocketChannel, Context> contexts = new HashMap<>();
    private static final Pattern QUIT = Pattern.compile("(\\r)?(\\n)?/quit$");

    private static void newConnection(Selector selector, SelectionKey key) throws IOException {
        SocketChannel socketChannel;
        try (ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel()) {
            socketChannel = serverSocketChannel.accept();
        }
        socketChannel
                .configureBlocking(false)
                .register(selector, SelectionKey.OP_READ);
        contexts.put(socketChannel, new Context());
    }

    private static void echo(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Context context = contexts.get(socketChannel);
        try {
            socketChannel.read(context.nioBuffer);
            context.nioBuffer.flip();
            context.currentLine = context.currentLine + Charset.defaultCharset().decode(context.nioBuffer);
            if (QUIT.matcher(context.currentLine).find()) {
                context.terminating = true;
            } else if (context.currentLine.length() > 16) {
                context.currentLine = context.currentLine.substring(8);
            }
            context.nioBuffer.flip();
            int count = socketChannel.write(context.nioBuffer);
            if (count < context.nioBuffer.limit()) {
                key.cancel();
                socketChannel.register(key.selector(), SelectionKey.OP_WRITE);
            } else {
                context.nioBuffer.clear();
                if (context.terminating) {
                    cleanup(socketChannel);
                }
            }
        } catch (IOException err) {
            err.printStackTrace();
            cleanup(socketChannel);
        }
    }

    private static void continueEcho(Selector selector, SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Context context = contexts.get(socketChannel);
        try {
            int remainingBytes = context.nioBuffer.limit() - context.nioBuffer.position();
            int count = socketChannel.write(context.nioBuffer);
            if (count == remainingBytes) {
                context.nioBuffer.clear();
                key.cancel();
                if (context.terminating) {
                    cleanup(socketChannel);
                } else {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        } catch (IOException err) {
            err.printStackTrace();
            cleanup(socketChannel);
        }
    }

    private static void cleanup(SocketChannel socketChannel) throws IOException {
        socketChannel.close();
        contexts.remove(socketChannel);
    }

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(3000));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        while (true) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    newConnection(selector, key);
                } else if (key.isReadable()) {
                    echo(key);
                } else if (key.isWritable()) {
                    continueEcho(selector, key);
                }
                it.remove();
            }
        }
    }

    private static class Context {
        private final ByteBuffer nioBuffer = ByteBuffer.allocate(512);
        private String currentLine = "";
        private boolean terminating = false;
    }

}
