package com.aperturescience.service.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.omg.IOP.Encoding;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
public class SerialServiceImpl implements SerialService {

    private SerialPort comPort;

    @Override
    public String sendMsg(String key, Integer value) {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        System.out.println("SEND: " +Arrays.toString(SerialPort.getCommPorts()));

        comPort.openPort();
        comPort.setBaudRate(9600);

        String request = key + ":" + value;
        request = request.trim();
        //Send
        comPort.writeBytes(request.getBytes(), request.getBytes().length);
        //Read
//        String response = blockingClient();
        comPort.closePort();
        System.out.println("SENT "+request);

//        final String[] response = {""};
//        comPort.addDataListener(new SerialPortDataListener() {
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//
//            @Override
//            public void serialEvent(SerialPortEvent event) {
//                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
//                    return;
//                byte[] newData = new byte[comPort.bytesAvailable()];
//                if (newData.length > 1) {
//                    response[0] = new String(newData);
//                    notifiyClient(new String(newData));
//                }
////                int numRead = comPort.readBytes(newData, newData.length);
//            }
//        });
//        comPort.closePort();
        //Writing key
//        return response[0];
        return "";
    }

    @Override
    public void readMsg() {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        System.out.println("RECIEVE: "+Arrays.toString(SerialPort.getCommPorts()));
        comPort.setBaudRate(9600);
        comPort.openPort();
        System.out.println("BAUD: "+comPort.getBaudRate());
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                byte[] newData = event.getReceivedData();
                System.out.println("Received data of size: " + newData.length);
                for (int i = 0; i < newData.length; ++i)
                    System.out.print((char)newData[i]+" < ");
                System.out.println("\n");
            }
        });
    }

    @Override
    public String blockingClient() {

        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.setBaudRate(9600);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
        try {
            while (true) {
                byte[] readBuffer = new byte[1024];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();

        return "";


//        SerialPort comPort = SerialPort.getCommPorts()[0];
//        System.out.println("Port: " + comPort.getSystemPortName());
//        comPort.setBaudRate(9600);
//        comPort.openPort();
//        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
//        try {
//            while (true) {
//                byte[] readBuffer = new byte[comPort.bytesAvailable()];
//                if (readBuffer.length > 1) {
//                    comPort.closePort();
//                    return new String(readBuffer);
//                }
//                Thread.sleep(20);
////                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
////                System.out.println("Read " + numRead + " bytes.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        comPort.closePort();

//        return "<<<<<";
    }


    private void notifiyClient(String msg) {
        System.out.println("Response: " + msg);
    }


    public static class Constants {
        private static final String motor1 = "MOTOR1";
        private static final String motor2 = "MOTOR2";
        private static final String motor3 = "MOTOR3";
        private static final String motor4 = "MOTOR4";
    }
}
