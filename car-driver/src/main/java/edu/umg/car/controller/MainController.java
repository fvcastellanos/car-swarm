package edu.umg.car.controller;

import com.google.common.collect.Queues;
import edu.umg.car.services.CarService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.Stack;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private CarService carService;

    private StringBuffer buffer;

    private Queue<String> commandQueue;

    public MainController() {
        buffer = new StringBuffer();

        commandQueue = Queues.newArrayDeque();
    }

    @FXML
    private TextArea edStatus;

    @FXML
    private TextField edMessage;

    @FXML
    public void initCom() {
        logger.info("init car");
        carService.init(this::addMessage);

        addMessage("opening serial port");
    }

    @FXML
    public void closeCom() {
        logger.info("closing car comm");
        carService.close();

        addMessage("serial port closed");
    }

    @FXML
    public void ping() {
        logger.info("testing serial communication");

        addMessage("send test message");

        carService.ping();
    }

    @FXML
    public void forward() {
        logger.info("moving forward");

        addMessage("moving forward");

        carService.forward();
    }

    @FXML
    public void backward() {
        logger.info("moving backward");

        addMessage("moving backward");

        carService.back();
    }

    @FXML
    public void left() {
        logger.info("moving left");

        addMessage("moving left");

        carService.left();
    }

    @FXML
    public void right() {
        logger.info("moving right");

        addMessage("moving right");

        carService.right();
    }

    @FXML
    public void stop() {
        logger.info("stopping the car");

        addMessage("stopping the car");

        carService.stop();
    }

    @FXML
    public void clearText() {
        clearMessage();
    }

    @FXML
    public void sendCustomMessage() {

        String message = edMessage.getText();
        if (!isEmpty(message)) {
            carService.sendCustomMessage(message);
            addMessage("message sent: " + message);
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void testRoute() {
        commandQueue.add("f");
        commandQueue.add("l");
        commandQueue.add("f");
        
    }

    private void addMessage(String message) {
        buffer.append("\n");
        buffer.append(message);
        edStatus.setText(buffer.toString());
    }

    private void clearMessage() {
        buffer = new StringBuffer();
        addMessage("");
    }
}
