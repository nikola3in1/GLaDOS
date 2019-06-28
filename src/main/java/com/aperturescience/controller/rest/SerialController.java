package com.aperturescience.controller.rest;

import com.aperturescience.service.serial.SerialService;
import com.fazecast.jSerialComm.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/serial/**")
public class SerialController {

    private final SerialService serialService;

    public SerialController(SerialService serialService) {
        this.serialService = serialService;
    }

//    @GetMapping("/serial/read")
//    public void serialRead() {
//
//        System.out.println("Reading msg from serial");
//        new Thread(serialService::readMsg).start();
//        System.out.println("Message's read");
//    }

    @GetMapping("/serial/write/{msg}")
    public void serialWrite(@PathVariable String msg) {
        /*
         * motor1:10
         * ;
         * motor1:10
         * ;
         * motor1:10
         * ;
         * motor1:10;
         *
         * */
        Long startTime = System.currentTimeMillis();
        msg = "motor1:1;motor2:1;motor3:1;motor4:1;";
        serialService.sendMsg(msg);
//        serialService.nonBlockingReading();

        Long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed: " + endTime);
    }

    @GetMapping("/serial/write/{motor}/{value}")
    public void serialWrite(@PathVariable String motor, @PathVariable Integer value) {
        /*
         * motor1:10
         * ;
         * motor1:10
         * ;
         * motor1:10
         * ;
         * motor1:10;
         *
         * */

        Long startTime = System.currentTimeMillis();
        String msg = motor + ":" + value;
        new Thread(serialService::nonBlockingReading);
        serialService.sendMsg(msg);
//        String msg = serialService.readMsg();
//        serialService.nonBlockingReading();
        Long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed: " + endTime);
    }

    @GetMapping("/serial")
    public void serialTest() {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        System.out.println("PORTS: " + Arrays.toString(SerialPort.getCommPorts()));
        comPort.openPort();
        comPort.setBaudRate(9600);
        try {
            while (true) {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
                System.out.println(new String(readBuffer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }
}
