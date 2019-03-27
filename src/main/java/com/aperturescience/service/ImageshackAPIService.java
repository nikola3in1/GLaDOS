package com.aperturescience.service;

import com.aperturescience.model.Image;

public interface ImageshackAPIService {
    String login();
    String upload(Image image);
    String getAuthToken();
}
