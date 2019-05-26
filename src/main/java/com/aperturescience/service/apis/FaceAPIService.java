package com.aperturescience.service.apis;

import com.aperturescience.model.response.FaceData;

public interface FaceAPIService {
    FaceData analyze(String picUrl);
}
