package edu.umg.car.serial;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

class SerialWriter {

    private static final Logger logger = LoggerFactory.getLogger(SerialWriter.class);

    private OutputStreamWriter outputStreamWriter;
    private BufferedWriter bufferedWriter;

    SerialWriter(OutputStream os) {
        this.outputStreamWriter = new OutputStreamWriter(os);
        this.bufferedWriter = new BufferedWriter(this.outputStreamWriter);
    }

    void writeMessage(String message) {

        Try.run(() -> {
            logger.info("sending message: {} to serial port");

            bufferedWriter.write(message);
        }).onFailure(ex -> logger.error("can't write to serial port: ", ex));
    }

    void close() {
        Try.run(() -> {
            bufferedWriter.close();
            outputStreamWriter.close();
        }).onFailure(ex -> logger.error("can't close writer: ", ex));
    }
}
