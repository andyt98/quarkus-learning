package com.andy.p4_event_streams.p1_streamapis;

import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;

/**
 * Demonstrates reading the contents of a file using the Vert.x asynchronous file APIs.
 * This method uses a non-blocking approach to read a file and print its contents to the console.
 * <p>
 * It employs Vert.x's powerful async model to handle file system operations without
 * blocking the calling thread. The use of handlers for data, error, and end of stream
 * allows the application to react to events rather than polling for data.
 * <p>
 * This non-blocking approach is more efficient for IO-bound operations, particularly
 * over large files or slow IO channels, as it allows the event loop to handle other tasks
 * while waiting for IO operations to complete.
 * <p>
 * Key points:
 * <ul>
 *   <li>{@code OpenOptions} is configured to specify file read permissions (1).</li>
 *   <li>The file system operation to open the file is asynchronous (2).</li>
 *   <li>{@code AsyncFile} represents an asynchronous file that can be read from or written to (3).</li>
 *   <li>Data handler is set up to handle the buffer of data read from the file (4).</li>
 *   <li>Exception handler is set up to handle any IO exceptions that may occur (5).</li>
 *   <li>End handler is called once the read stream is finished (6).</li>
 * </ul>
 */

public class VertxStreams {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        OpenOptions opts = new OpenOptions().setRead(true);                            // (1)
        vertx.fileSystem().open("build.gradle.kts", opts, ar -> {   // (2)
            if (ar.succeeded()) {
                AsyncFile file = ar.result();                                          // (3)
                file.handler(System.out::println)                               // (4)
                        .exceptionHandler(Throwable::printStackTrace)           // (5)
                        .endHandler(done -> {                                // (6)
                            System.out.println("\n--- DONE");
                            vertx.close();
                        });
            } else {
                ar.cause().printStackTrace();
            }
        });
    }
}
