package com.aperturescience.controller;

import com.aperturescience.model.response.FaceData;
import com.aperturescience.service.camera.CamService;
import com.aperturescience.service.apis.FaceAPIService;
import com.aperturescience.service.apis.ImageshackAPIService;
import com.aperturescience.service.apis.ObjectDetectionAPIService;
import com.aperturescience.service.serial.SerialService;
import com.fazecast.jSerialComm.SerialPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/*This class is purely used for testing purposes*/
@RestController
@RequestMapping("/api/**")
public class RESTController {

    // Dependencies
    private final ImageshackAPIService imgApiService;
    private final CamService camService;
    private final ObjectDetectionAPIService objectDetectionAPIService;
    private final FaceAPIService faceAPIService;
    private final SerialService serialService;

    public RESTController(ImageshackAPIService imgApiService, CamService camService,
                          ObjectDetectionAPIService objectDetectionAPIService, FaceAPIService faceAPIService,
                          SerialService serialService) {
        this.imgApiService = imgApiService;
        this.serialService = serialService;
        this.camService = camService;
        this.objectDetectionAPIService = objectDetectionAPIService;
        this.faceAPIService = faceAPIService;
    }

    @GetMapping("/login")
    public String login() {
        imgApiService.login();
        return imgApiService.getAuthToken();
    }

    @GetMapping("/analyze")
    public String upload() {
        imgApiService.login();
        String picUrl = imgApiService.upload(camService.capture());

        System.out.println("picture url:" + picUrl);
        FaceData data = faceAPIService.analyze(picUrl);
        drawRect(picUrl, data);
        return "done";
    }

    private void drawRect(String imageUrl, FaceData faceData) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new URL(imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.RED);
        g2d.drawRect(faceData.getFaceRectangle().getLeft(), faceData.getFaceRectangle().getTop()
                , faceData.getFaceRectangle().getWidth(), faceData.getFaceRectangle().getHeight());
        g2d.dispose();

        try {
            ImageIO.write(img,"jpg",new File("/home/nikola3in1/Desktop/test.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/serialRead")
    public void serialRead(){
        System.out.println("Reading msg from serial");
        serialService.readMsg();
        System.out.println("Message's read");
    }


    @GetMapping("/serial/write/{motor}/{value}")
    public void serialWrite(@PathVariable String motor, @PathVariable Integer value){
        System.out.println("Sending: "+motor+", "+value);
        System.out.println("Reading msg from serial");


        serialService.readMsg();
        serialService.sendMsg(motor, value);
        System.out.println("Message's read");
    }

    @GetMapping("/serial")
    public void serialTest(){
        SerialPort comPort = SerialPort.getCommPorts()[0];
        System.out.println("PORTS: "+Arrays.toString(SerialPort.getCommPorts()));
        comPort.openPort();
        comPort.setBaudRate(57600);
        try {
            while (true)
            {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }

    private String test() {
        imgApiService.login();
        Long startTime = System.currentTimeMillis();
        String response = "";

        for (int i = 0; i < 50; i++) {
            response += imgApiService.upload(camService.capture()) + "\n";
        }

        Long takenTime = System.currentTimeMillis() - startTime;
        response += "\nTime taken: " + takenTime;
        return "\nResponse: " + response;
    }
}
