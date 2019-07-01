package com.aperturescience.service.serial;

import com.aperturescience.model.MotorStates;
import com.aperturescience.service.apis.FaceAPIService;
import com.aperturescience.service.apis.ImageshackAPIService;
import com.aperturescience.service.camera.CamService;
import com.aperturescience.service.robot.RobotControlerService;
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
    private final RobotControlerService robotService;

    public SerialServiceImpl(DataPersistenceService dataService, SocketCommunicationService clientService, RobotControlerService robotService) {
        this.dataService = dataService;
        this.clientService = clientService;
        this.robotService = robotService;
    }

    // Read and write from serial
    @Override
    public void sendMsg(String msg) {
        msg += ";";
        System.out.println("Sending msg: " + msg);
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
        if (msg.contains(Constants.POSITIONS)) {
            MotorStates states = parsePostitions(msg);
            dataService.setCurrentStates(states);
            System.out.println(dataService.getCurrentStates());

        } else if (msg.contains(":")) {
            boolean updateClients = false;

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
            } else if (msg.contains(Constants.MOTION)) {
                //TESTING
//                MotorStates newMotorStates = robotService.findFace();
//                // We found face!
//                if (newMotorStates != null) {
//                    // send new MotorStates
//                    updateClients = true;
//                    System.out.println("New motor states: " + newMotorStates);
////                    sendMsg(Messages.setMin);
//                    test1();
//                }
            }


            // Motor updated
            if (msg.contains("motor")) {

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

    private void test1() {
        sendMsg(Messages.setMin);
        sendMsg("motor4:68");
    }

    private MotorStates parsePostitions(String msg) {
        String[] tokens = msg.split(":");
        //POS:90:90:90:60
        int head = Integer.parseInt(tokens[4]);
        int neck = Integer.parseInt(tokens[3]);
        int elbow = Integer.parseInt(tokens[2]);
        int base = Integer.parseInt(tokens[1]);
        MotorStates states = new MotorStates(head, neck, elbow, base);
        return states;
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
            System.out.println("PORT: " + this.comPort);
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
        private static final String POSITIONS = "POS";
        private static final String MOTOR1 = "MOTOR1";
        private static final String MOTOR2 = "MOTOR2";
        private static final String MOTOR3 = "MOTOR3";
        private static final String MOTOR4 = "MOTOR4";
        private static final String MOTION = "MOTION";
        private static final String HUMIDITY = "HUMIDITY";
        private static final String TEMPERATURE = "TEMPERATURE";
        private static final String LIGHT = "LIGHT";
    }

    public static class Messages{
        public static final String setIdle = "IDLE";
        public static final String setMin = "motor3:-18;motor4:-39";
        public static final String rangeTest = "motor3:44;motor4:68;motor3:-44;motor4:-68";
        public static final String POSITIONS = "POS";
    }
}
