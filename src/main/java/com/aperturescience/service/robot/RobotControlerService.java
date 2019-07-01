package com.aperturescience.service.robot;

import com.aperturescience.model.MotorStates;
import com.aperturescience.model.response.FaceData;

public interface RobotControlerService {
    MotorStates findFace();
    MotorStates calculateCorrection(MotorStates currentMotorStates, FaceData faceData);
}
