package com.aperturescience.service;

import com.aperturescience.model.response.FaceData;

public interface FaceAPIService {
    FaceData analyze(String picUrl);
}
