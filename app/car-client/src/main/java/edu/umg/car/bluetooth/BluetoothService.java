package edu.umg.car.bluetooth;

import edu.umg.car.bluetooth.listeners.BtDeviceListener;
import edu.umg.car.bluetooth.listeners.BtServicesUrlListener;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Optional;

public class BluetoothService {

    private static final Logger logger = LoggerFactory.getLogger(BluetoothService.class);

    private BtServicesUrlListener btServicesUrlListener;

    public BluetoothService(BtServicesUrlListener btServicesUrlListener) {
        this.btServicesUrlListener = btServicesUrlListener;
    }

    public Optional<RemoteDevice> inquiryDevice(String name) {

        return Try.of(() -> {

            BtDeviceListener btDeviceListener = new BtDeviceListener(name);

            synchronized (BtDeviceListener.inquiryCompletedEvent) {
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, btDeviceListener);

                if (started) {
                    logger.info("scan started");
                    BtDeviceListener.inquiryCompletedEvent.wait();
                }
            }

            return btDeviceListener.getRemoteDeviceHolder();

        }).onFailure(ex -> logger.error("can't get device: ", ex)).get();
    }

    public String getServiceURL(String name) {

        Optional<RemoteDevice> remoteDeviceHolder = inquiryDevice(name);

        if (remoteDeviceHolder.isPresent()) {

            RemoteDevice remoteDevice = remoteDeviceHolder.get();
            Try.of(() -> {

                Tuple2<int[], UUID[]> tuple2 = configureServices();

                int[] attrIDs = tuple2._1();
                UUID[] searchUuidSet = tuple2._2();

                synchronized (BtServicesUrlListener.serviceSearchCompletedEvent) {

                    logger.info("looking for bt services");
                    LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet,
                            remoteDevice, btServicesUrlListener);
                    BtServicesUrlListener.serviceSearchCompletedEvent.wait();
                    logger.info("completed acquiring services");
                }

                return btServicesUrlListener.getUrl();

            }).onFailure(ex -> logger.info("can't get service url: ", ex));
        }

        return "";
    }

    public String sendChar(String url, String message) {
        return Try.of(() -> {
            StreamConnection streamConnection = (StreamConnection) Connector.open(url);
            OutputStream os = streamConnection.openOutputStream();
            InputStream is = streamConnection.openInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(streamConnection.openInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            os.write(message.getBytes()); //just send 'message' to the device
            Thread.sleep(500);
            String response = bufferedReader.readLine();

            bufferedReader.close();
            inputStreamReader.close();

            os.close();
            is.close();
            streamConnection.close();

            return response;
        }).onFailure(ex -> logger.error("can't read / write into device: ", ex))
                .getOrElse("");
    }

    private static Tuple2<int[], UUID[]> configureServices() {

        UUID uuid = new UUID(0x1101); //scan for btspp://... services (as HC-05 offers it)
        UUID[] searchUuidSet = new UUID[] { uuid };

        int[] attrIDs = new int[]{
                0x0100 // service name
        };

        return new Tuple2<>(attrIDs, searchUuidSet);
    }

}
