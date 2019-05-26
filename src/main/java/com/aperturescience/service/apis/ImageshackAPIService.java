package com.aperturescience.service.apis;

import com.aperturescience.model.Image;

public interface ImageshackAPIService {
    String login();
    String upload(Image image);
    String getAuthToken();
}
