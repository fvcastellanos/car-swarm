package edu.umg.car.services;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import edu.umg.car.listener.LoggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
public class ComService {

    private static final Logger logger = LoggerFactory.getLogger(ComService.class);

    @Value("${com.port}")
    private String comPort;

    private SerialPortDataListener serialPortDataListener;
    private SerialPort serialPort;

    public ComService() {
    }

    public boolean initPort() {

        logger.info("init serial port in : {}", comPort);
        serialPort = SerialPort.getCommPort(comPort);

        if (serialPort.openPort()) {
            serialPortDataListener = new LoggerListener(serialPort);
            serialPort.addDataListener(serialPortDataListener);
            logger.info("port: {} is open", comPort);
        }

        return serialPort.isOpen();
    }

    public void closePort() {

        logger.info("closing serial port: {}", comPort);
        serialPort.removeDataListener();

        if (serialPort.closePort()) {
            logger.info("serial port closed");
        }
    }

    public void write(String message) {

        logger.info("attempting to write: {} into port: {}", message, comPort);
        byte[] bytes = message.getBytes(Charset.forName("utf-8"));
        serialPort.writeBytes(bytes, bytes.length);

        logger.info("write succeeded");
    }
}
