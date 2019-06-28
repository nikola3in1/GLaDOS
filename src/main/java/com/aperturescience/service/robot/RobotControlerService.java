package com.aperturescience.service.robot;

import com.aperturescience.model.MotorStates;
import com.aperturescience.model.response.FaceData;

public interface RobotControlerService {

    MotorStates calculateCorrection(MotorStates currentMotorStates, FaceData faceData);
}
