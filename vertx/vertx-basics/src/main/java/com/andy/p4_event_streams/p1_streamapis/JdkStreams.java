package com.andy.p4_event_streams.p1_streamapis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Demonstrates reading the contents of a file using Java's classic I/O APIs.
 * It reads data into a buffer and prints it to the console until the end of the file is reached.
 * This is a blocking I/O operation which will hold the executing thread until the I/O operation is complete.
 * <p>
 * This example uses a try-with-resources statement to ensure that the FileInputStream
 * is closed properly at the end of the operation, regardless of whether an exception is thrown.
 * <p>
 * It is important to note that this approach is not suitable for handling large files
 * as it reads the file content into memory. For larger files, a different approach that
 * processes the file in chunks without holding it entirely in memory would be required.
 */

public class JdkStreams {

    public static void main(String[] args) {
        File file = new File("build.gradle.kts");
        byte[] buffer = new byte[1024];
        try (FileInputStream in = new FileInputStream(file)) {
            int count = in.read(buffer);
            while (count != -1) {
                System.out.println(new String(buffer, 0, count));
                count = in.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\n--- DONE");
        }
    }
}
