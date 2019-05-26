package com.aperturescience.controller;

import com.aperturescience.model.response.FaceData;
import com.aperturescience.service.camera.CamService;
import com.aperturescience.service.apis.FaceAPIService;
import com.aperturescience.service.apis.ImageshackAPIService;
import com.aperturescience.service.apis.ObjectDetectionAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/*This class is purely used for testing purposes*/
@RestController
@RequestMapping("/rest/api/**")
public class RESTController {

    @Autowired
    private ImageshackAPIService imgApiService;

    @Autowired
    private CamService camService;

    @Autowired
    private ObjectDetectionAPIService objectDetectionAPIService;

    @Autowired
    private FaceAPIService faceAPIService;

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
