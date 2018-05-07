package edu.umg.car.bluetooth.listeners;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class BtServicesUrlListener implements DiscoveryListener {

    private static final Logger logger = LoggerFactory.getLogger(BtServicesUrlListener.class);

    public static final Object serviceSearchCompletedEvent = new Object();

    private String url;

    public BtServicesUrlListener() {
        url = "";
    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {

    }

    @Override
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

        logger.info("service search started");
        ServiceRecord serviceRecord = Stream.of(serviceRecords)
                .filter(service -> {
                    url = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    logger.info("verifying service: {}", url);

                    return StringUtils.isEmpty(url);
                })
                .findFirst()
                .orElse(null);

    }

    @Override
    public void serviceSearchCompleted(int i, int i1) {
        logger.info("service search completed!");
        synchronized(serviceSearchCompletedEvent){
            serviceSearchCompletedEvent.notifyAll();
        }
    }

    @Override
    public void inquiryCompleted(int i) {

    }

    public String getUrl() {
        return url;
    }
}
