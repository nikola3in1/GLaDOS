package com.aperturescience.service.robot;

import com.aperturescience.model.MotorStates;
import com.aperturescience.model.response.FaceData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RobotControlerServiceImpl implements RobotControlerService {

    @Value("${robot.camera.resolution.width}")
    private Integer cameraWidth;

    @Value("${robot.camera.resolution.width}")
    private Integer cameraHeight;

    @Value("${robot.camera.motor.step.size}")
    private Integer motorStepSize;

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

    private int calcNumberOfSteps(int correction) {
        return correction / motorStepSize;
    }
}
