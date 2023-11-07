package com.andy.p4_event_streams.p4_parsing_streams;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates the process of reading a serialized database file and
 * parsing its contents using a RecordParser which can dynamically switch between
 * different parsing modes.
 * <p>
 * The serialized database format contains a magic number, a version, the database
 * name, and a sequence of key-value pairs. Each key-value pair includes an integer
 * that represents the key's length, the key itself, an integer for the value's length,
 * and the value.
 * <p>
 * The parser switches between fixed size mode for reading lengths and magic numbers,
 * and delimited mode for reading the database name and values, depending on the
 * structure of the next segment in the stream.
 */

public class DatabaseReader {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseReader.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        AsyncFile file = vertx.fileSystem().openBlocking("sample.db",
                new OpenOptions().setRead(true));

        RecordParser parser = RecordParser.newFixed(4, file);
        parser.handler(header -> readMagicNumber(header, parser));
        parser.endHandler(v -> vertx.close());
    }

    private static void readMagicNumber(Buffer header, RecordParser parser) {
        logger.info("Magic number: {}:{}:{}:{}", header.getByte(0), header.getByte(1), header.getByte(2), header.getByte(3));
        parser.handler(version -> readVersion(version, parser));
    }

    private static void readVersion(Buffer header, RecordParser parser) {
        logger.info("Version: {}", header.getInt(0));
        parser.delimitedMode("\n");
        parser.handler(name -> readName(name, parser));
    }

    private static void readName(Buffer name, RecordParser parser) {
        logger.info("Name: {}", name.toString());
        parser.fixedSizeMode(4);
        parser.handler(keyLength -> readKey(keyLength, parser));
    }

    private static void readKey(Buffer keyLength, RecordParser parser) {
        parser.fixedSizeMode(keyLength.getInt(0));
        parser.handler(key -> readValue(key.toString(), parser));
    }

    private static void readValue(String key, RecordParser parser) {
        parser.fixedSizeMode(4);
        parser.handler(valueLength -> finishEntry(key, valueLength, parser));
    }

    private static void finishEntry(String key, Buffer valueLength, RecordParser parser) {
        parser.fixedSizeMode(valueLength.getInt(0));
        parser.handler(value -> {
            logger.info("Key: {} / Value: {}", key, value);
            parser.fixedSizeMode(4);
            parser.handler(keyLength -> readKey(keyLength, parser));
        });
    }
}
