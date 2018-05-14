package edu.umg.car.listener;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class LoggerListener implements SerialPortDataListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggerListener.class);

    private SerialPort serialPort;

    private StringBuffer buffer;

    private Consumer<String> messageAction;

    public LoggerListener(SerialPort serialPort) {
        this.serialPort = serialPort;
        this.buffer = new StringBuffer();
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

        if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;

        byte[] newData = new byte[serialPort.bytesAvailable()];
        int numRead = serialPort.readBytes(newData, newData.length);

        addToBuffer(newData);
    }

    public void setMessageAction(Consumer<String> consumer) {
        this.messageAction = consumer;
    }

    private void addToBuffer(byte[] data) {
        buffer.append(new String(data));

        for (byte bt : data) {
            if (bt == '\n') {
                logger.info("read data: {}", buffer.toString());

                if (!isNull(messageAction)) {
                    messageAction.accept(buffer.toString());
                }

                buffer = new StringBuffer();
            }
        }
    }
}
