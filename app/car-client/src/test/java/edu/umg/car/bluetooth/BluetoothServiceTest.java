package edu.umg.car.bluetooth;

import edu.umg.car.config.AppConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.bluetooth.RemoteDevice;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class BluetoothServiceTest {

//    @Autowired
//    private BluetoothService bluetoothService;


    @Before
    public void setup() {
    }

    @Test
    public void testGetServiceUrl() {

//        String url = bluetoothService.getServiceURL("HC");

        String url = "hola";
        System.out.println("URL: " + url);

    }

//    @Test
    public void testSendPing() {
//        String response = bluetoothService.sendChar("btspp://98D33490A262:1;authenticate=false;encrypt=false;master=false", "p");
//
//        System.out.println("response: " + response);
    }
}
