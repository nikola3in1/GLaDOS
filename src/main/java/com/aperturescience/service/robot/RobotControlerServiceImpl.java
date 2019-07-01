package com.aperturescience.service.robot;

import com.aperturescience.model.Image;
import com.aperturescience.model.MotorStates;
import com.aperturescience.model.response.FaceData;
import com.aperturescience.service.apis.FaceAPIService;
import com.aperturescience.service.apis.ImageshackAPIService;
import com.aperturescience.service.camera.CamService;
import com.aperturescience.service.serial.SerialService;
import com.aperturescience.service.serial.SerialServiceImpl;
import com.aperturescience.service.state.DataPersistenceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class RobotControlerServiceImpl implements RobotControlerService {

    @Value("${robot.camera.resolution.width}")
    private Integer cameraWidth;

    @Value("${robot.camera.resolution.width}")
    private Integer cameraHeight;

    @Value("${robot.camera.motor.step.size}")
    private Integer motorStepSize;

    private Long lastSearchTimestamp = System.currentTimeMillis();
    private Long searchDelay = 10000L; // 5 sec

    private final ImageshackAPIService imgApiService;
    private final FaceAPIService faceAPIService;
    private final CamService camService;
    private final DataPersistenceService persistenceService;

    public RobotControlerServiceImpl(ImageshackAPIService imageshackAPIService, FaceAPIService faceAPIService, CamService camService, DataPersistenceService persistenceService) {
        this.imgApiService = imageshackAPIService;
        this.faceAPIService = faceAPIService;
        this.camService = camService;
        this.persistenceService = persistenceService;
    }

    @Override
    public MotorStates calculateCorrection(MotorStates currentMotorStates, FaceData faceData) {
//        // Scale factors 'scaleWidth' & 'scaleHeight' determines
//        // how close the face is or how big the face marker rect is.
//        int scaleWidth, scaleHeight;
//        scaleWidth = cameraWidth / faceData.getFaceRectangle().getWidth();
//        scaleHeight = cameraHeight / faceData.getFaceRectangle().getHeight();

        int faceCenterX = faceData.getFaceRectangle().getWidth() / 2;
        int faceCenterY = faceData.getFaceRectangle().getHeight() / 2;

        int correctionX = cameraWidth / 2 - faceCenterX;
        int correctionY = cameraHeight / 2 - faceCenterY;

        int numberOfStepsX = calcNumberOfSteps(correctionX);
        int numberOfStepsY = calcNumberOfSteps(correctionY);

        // motorX1 - base
        // motorX2 - neck
        // motorY1 - elbow
        // motorY2 - head

        int motorX1 = currentMotorStates.getBase();
        int motorX2 = currentMotorStates.getNeck();
        int motorY1 = currentMotorStates.getElbow();
        int motorY2 = currentMotorStates.getHead();

        //
        return null;
    }

    @Override
    public MotorStates findFace() {
        long currentTime = System.currentTimeMillis();
        if (lastSearchTimestamp + searchDelay < currentTime) {
            System.out.println("Searching face");
            MotorStates newMotorStates = new MotorStates();

            imgApiService.login();
            Image img = camService.capture();
            String picUrl = imgApiService.upload(img);
            FaceData data = faceAPIService.analyze(picUrl);

            // We found face!
            if (data != null && data.getFaceRectangle() != null) {
                System.out.println("WE FOUND FACE");
                newMotorStates = calculateCorrection(persistenceService.getCurrentStates(), data);
                drawRect(picUrl,data);
            }

            // Update last taken picture in data persistence service
            persistenceService.setLastPicUrl(picUrl);
            // Update last search timestamp
            lastSearchTimestamp = System.currentTimeMillis();

            return newMotorStates;
        }else{
//            System.out.println("Timeout!");
        }
        return null;
    }

    private void drawRect(String imageUrl, FaceData faceData) {
        if (faceData == null && faceData.getFaceRectangle() == null) {
            return;
        }

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
            ImageIO.write(img,"jpg",new File("./test.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calcNumberOfSteps(int correction) {
        return correction / motorStepSize;
    }
}
