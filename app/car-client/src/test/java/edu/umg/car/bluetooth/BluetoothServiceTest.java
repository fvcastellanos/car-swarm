package edu.umg.car.bluetooth;

import org.junit.Before;
import org.junit.Test;

public class BluetoothServiceTest {

    private BluetoothService bluetoothService;


    @Before
    public void setup() {
        bluetoothService = new BluetoothService();
    }

    @Test
    public void testBluetoothClient() throws Exception {
        bluetoothService.cosa();


    }
}
