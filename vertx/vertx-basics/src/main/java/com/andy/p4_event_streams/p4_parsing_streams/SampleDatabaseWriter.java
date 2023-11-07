package com.andy.p4_event_streams.p4_parsing_streams;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;

/**
 * This class demonstrates how to write data to a file using a specific serialization
 * scheme for key-value entries in a database-like structure.
 * The database stream consists of a header with a magic number, version, and name,
 * followed by a series of key-value pairs.
 */
public class SampleDatabaseWriter {

    /**
     * The main method that writes a sample database to a file named 'sample.db'.
     * The file consists of a magic number, a version number, the database name,
     * and key-value pairs, where each key and value is prefixed with an integer
     * representing its length.
     * <p>
     * The format is intended to illustrate the use of Vert.x's Buffer and FileSystem
     * APIs to write mixed binary and text data to a file.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        AsyncFile file = vertx.fileSystem().openBlocking("sample.db",
                new OpenOptions().setWrite(true).setCreate(true));

        Buffer buffer = Buffer.buffer();

        // Magic number to identify the file type
        buffer.appendBytes(new byte[]{1, 2, 3, 4});

        // Version of the database stream format
        buffer.appendInt(2);

        // Name of the database ending with a newline character
        buffer.appendString("Sample database\n");

        // First key-value pair where the key is 'abc' and the value is '123456-abcdef'
        String key = "abc";
        String value = "123456-abcdef";
        buffer
                .appendInt(key.length())
                .appendString(key)
                .appendInt(value.length())
                .appendString(value);

        // Second key-value pair where the key is 'foo@bar' and the value is 'Foo Bar Baz'
        key = "foo@bar";
        value = "Foo Bar Baz";
        buffer
                .appendInt(key.length())
                .appendString(key)
                .appendInt(value.length())
                .appendString(value);

        // Writes the buffer to the file and closes the Vert.x instance upon completion
        file.end(buffer, ar -> vertx.close());
    }
}
