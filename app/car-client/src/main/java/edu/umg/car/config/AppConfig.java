package edu.umg.car.config;

import edu.umg.car.bluetooth.BluetoothService;
import edu.umg.car.bluetooth.listeners.BtServicesUrlListener;
import edu.umg.car.serial.SerialCommService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

//    @Bean
//    public BtServicesUrlListener btServicesUrlListener() {
//        return new BtServicesUrlListener();
//    }
//
//    @Bean
//    public BluetoothService bluetoothService(BtServicesUrlListener btServicesUrlListener) {
//        return new BluetoothService(btServicesUrlListener);
//    }

    @Bean
    public SerialCommService serialCommService() {
        return new SerialCommService();
    }

}
