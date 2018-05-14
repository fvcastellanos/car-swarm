package edu.umg.car.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    private ComService comService;

    @Value("${command.forward}")
    private String moveForward;

    @Value("${command.backward}")
    private String moveBackward;

    @Value("${command.left}")
    private String moveLeft;

    @Value("${command.right}")
    private String moveRight;

    @Value("${command.stop}")
    private String stop;

    @Value("${command.ping}")
    private String ping;

    @Autowired
    public CarService(ComService comService) {
        this.comService = comService;

        comService.initPort();
    }

    public void forward() {

        logger.info("moving car forward");
        comService.write(moveForward);
    }

    public void back() {

        logger.info("moving car backwards");
        comService.write(moveBackward);
    }

    public void left() {

        logger.info("turn car left");
        comService.write(moveLeft);
    }

    public void right() {

        logger.info("turn car right");
        comService.write(moveRight);
    }

    public void stop() {

        logger.info("stopping car");
        comService.write(stop);
    }

    public void ping() {

        logger.info("checking aliveness");
        comService.write(ping);
    }

    public void close() {

        logger.info("closing communication");
        comService.closePort();
    }
}
