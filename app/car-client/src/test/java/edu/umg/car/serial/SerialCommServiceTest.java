package edu.umg.car.serial;

import edu.umg.car.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SerialCommServiceTest extends TestBase {

    @Autowired
    private SerialCommService serialCommService;

    @Test
    public void testCommConnect() {

        serialCommService.connect("COM5");

    }

    @Test
    public void testPing() {
        serialCommService.writeMessage("p");
    }

    @Test
    public void testDisconnect() {
        serialCommService.close("COM5");
    }
}
