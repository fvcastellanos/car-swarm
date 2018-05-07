package edu.umg.car.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

public class SerialCommService {

    private final static Logger logger = LoggerFactory.getLogger(SerialCommService.class);

    private InputStream in;
    private OutputStream out;
    private SerialReader serialReader;
    private SerialWriter serialWriter;
    private SerialPort serialPort;

    public void connect(String portName) {

        Try.run(() -> {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            if (!portIdentifier.isCurrentlyOwned()) {

                CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                if (commPort instanceof SerialPort) {

                    serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    in = serialPort.getInputStream();
                    out = serialPort.getOutputStream();

                    serialReader = new SerialReader(in);
                    serialWriter = new SerialWriter(out);

                    serialPort.addEventListener(serialReader);
                    serialPort.notifyOnDataAvailable(true);
                }

                logger.error("only serial ports are supported");
            }

            logger.error("selected port is busy");
        }).onFailure(ex -> logger.error("can't open port: ", ex));
    }

    public void close(String portName) {

        Try.run(() -> {

            logger.info("trying to close serial port");
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            if (portIdentifier.isCurrentlyOwned()) {
                logger.info("closing streams");
                serialPort.removeEventListener();
                serialReader.close();
                serialWriter.close();

                in.close();
                out.close();

                serialPort.close();

                logger.info("everything is close (I think...)");
            }

        }).onFailure(ex -> logger.error("can't close serial port: ", ex));
    }

    public void writeMessage(String message) {
        Try.run(() -> {
            serialWriter.writeMessage(message);
        }).onFailure(ex -> logger.error("can't send write a message: ", ex));
    }
}
