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
    @GetMapping("/read/pos")
    public void getPositions() {
        String msg = "POS";
        serialService.sendMsg(msg);
        System.out.println("send pos message");
    }

    @GetMapping("/test/min")
    public void testMin(){
        //motor3:44;motor4:68;motor3:-44;motor4:-68;
        System.out.println("Testing min ranges");
        String msg = "motor3:-18;motor4:-39";
        serialService.sendMsg(msg);
    }

    @GetMapping("/test/range")
    public void testRange(){
        //motor3:44;motor4:68;motor3:-44;motor4:-68;
        System.out.println("Testing range");
        String msg = "motor3:44;motor4:68;motor3:-44;motor4:-68";
        serialService.sendMsg(msg);
    }

    @GetMapping("/write/{head}/{neck}")
    public void serialWrite(@PathVariable String head, @PathVariable String neck) {
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
        String msg = "motor4:"+head+";motor3:"+neck;
        serialService.sendMsg(msg);
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
