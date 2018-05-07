package edu.umg.car.bluetooth.listeners;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.util.Optional;

public class BtDeviceListener implements DiscoveryListener {

    private Logger logger = LoggerFactory.getLogger(BtDeviceListener.class);

    public static final Object inquiryCompletedEvent = new Object();

    private RemoteDevice device;
    private String regex;

    public BtDeviceListener() {
        this.regex = "";
    }

    public BtDeviceListener(String regex) {
        this.regex = regex;
    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        try {
            String name = remoteDevice.getFriendlyName(false);
            logger.info("Name: {} - Bluetooth address: {}", name, remoteDevice.getBluetoothAddress());

            if(name.toLowerCase().startsWith(regex.toLowerCase())) {
//            if (name.matches("HC.*")) {
                device = remoteDevice;
                logger.info("filtered bt: {} found", name);
            }
        } catch (Exception e) {
            logger.error("can't get devices: ", e);
        }
    }

    @Override
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
    }

    @Override
    public void serviceSearchCompleted(int i, int i1) {
    }

    @Override
    public void inquiryCompleted(int i) {
        logger.info("device inquiry completed!");

        synchronized(inquiryCompletedEvent){
            inquiryCompletedEvent.notifyAll();
        }
    }

    public Optional<RemoteDevice> getRemoteDeviceHolder() {
        return Optional.ofNullable(device);
    }

    public String getRegex() {
        return regex;
    }
}
