package com.aperturescience.service.serial;

import com.aperturescience.service.state.DataPersistenceService;
import com.aperturescience.service.state.SocketCommunicationService;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SerialServiceImpl implements SerialService {

    private SerialPort comPort;
    private final Integer baudRate = 9600;

    private final DataPersistenceService dataService;
    private final SocketCommunicationService clientService;

    public SerialServiceImpl(DataPersistenceService dataService, SocketCommunicationService clientService) {
        this.dataService = dataService;
        this.clientService = clientService;
    }

    // Read and write from serial
    @Override
    public void sendMsg(String msg) {
        System.out.println("Sending msg");
        comPort.setBaudRate(9600);
        //Send
        comPort.writeBytes(msg.getBytes(), msg.getBytes().length);
        System.out.println("Msg's sent");
    }

    @Override
    public void readMsg() {
        System.out.println("Reading msg");
        System.out.println("PORTS: " + Arrays.toString(SerialPort.getCommPorts()));

        // Validation
        if (comPort == null) {
            System.out.println("NO AVAILABLE SERIAL PORTS");
            return;
        }

        if (!comPort.isOpen()) {
            comPort.openPort();
        }
        try {
            while (true) {
                while (comPort.bytesAvailable() <= 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                String msg = new String(readBuffer);

                String[] msgs = msg.split(";");
                for (String m : msgs) {
                    if (!m.isEmpty() && m.length() > 5) {
                        processMessage(m);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processMessage(String msg) {
        // HUMIDITY:10
        if (msg.contains(":")) {
            boolean updateClients = false;

//            System.out.println("MSG: " + msg);
            String valueStr = msg.split(":")[1].trim();
            int msgVal = Integer.parseInt(valueStr);
            Integer intVal = Math.round(msgVal);

            if (msg.contains(Constants.HUMIDITY)) {
                dataService.insertHumidity(intVal);
                updateClients = true;
            } else if (msg.contains(Constants.TEMPERATURE)) {
                dataService.insertTemperature(intVal);
                updateClients = true;
            } else if (msg.contains(Constants.LIGHT)) {
                dataService.insertBrightness(intVal);
                updateClients = true;
            }

            if (updateClients) {
                // Updating clients with current data
                clientService.sendCurrentData();
            }

        } else if (msg.contains("Error")) {
            try {
                throw new Exception("ERROR ON DHT SENSOR");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nonBlockingReading() {
        System.out.println("NON BLOCKING");
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] newData = event.getReceivedData();
                System.out.println("Received data of size: " + newData.length);
                for (int i = 0; i < newData.length; ++i)
                    System.out.print((char) newData[i]);
                System.out.println("\n");
            }
        });
    }

    // SerialPort methods
    public void initSerialPort() {
        System.out.println("Initializing serial port");
        System.out.println("NR OF PORTS: " + SerialPort.getCommPorts().length);
        if (SerialPort.getCommPorts().length > 0) {
            this.comPort = SerialPort.getCommPorts()[0];
            System.out.println("PORT: "+this.comPort);
            comPort.setBaudRate(baudRate);
            comPort.openPort();
        }
    }

    private void closePort() {
        comPort.closePort();
    }

    // Notifications
    private void notifiyClients(String msg) {
        System.out.println("Response: " + msg);
    }

    public static class Constants {
        private static final String MOTOR1 = "MOTOR1";
        private static final String MOTOR2 = "MOTOR2";
        private static final String MOTOR3 = "MOTOR3";
        private static final String MOTOR4 = "MOTOR4";
        private static final String HUMIDITY = "HUMIDITY";
        private static final String TEMPERATURE = "TEMPERATURE";
        private static final String LIGHT = "LIGHT";
    }
}
