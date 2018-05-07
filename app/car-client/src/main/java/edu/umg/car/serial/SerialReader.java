package edu.umg.car.serial;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class SerialReader implements SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SerialReader.class);

    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    SerialReader(InputStream is) {
        this.inputStreamReader = new InputStreamReader(is);
        this.bufferedReader = new BufferedReader(inputStreamReader);
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

        logger.info("serial event raised type: {}", serialPortEvent.getEventType());

        Try.of(() -> {

            String data = bufferedReader.readLine();

            logger.info("data read: {}", data);

            return data;
        }).onFailure(ex -> logger.error("can't read from serial port: ", ex));
    }

    void close() {

        Try.run(() -> {
            this.bufferedReader.close();
            this.inputStreamReader.close();
        }).onFailure(ex -> logger.error("can't close readr", ex));
    }
}
