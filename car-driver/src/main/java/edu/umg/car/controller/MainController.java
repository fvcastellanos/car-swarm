package edu.umg.car.controller;

import edu.umg.car.services.CarService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private CarService carService;

    private StringBuffer buffer;

    public MainController() {
        buffer = new StringBuffer();
    }

    @FXML
    private TextArea edStatus;

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
    public void exit() {
        System.exit(0);
    }

    private void addMessage(String message) {
        buffer.append("\n");
        buffer.append(message);
        edStatus.setText(buffer.toString());
    }
}
