package com.aperturescience.service;

import com.aperturescience.model.response.DetectedObjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
public class ObjectDetectionAPIServiceImpl implements ObjectDetectionAPIService {

    private final String apiKey = "cd27b3817f9246d58b157924a1bc75da";
    private final String region = "westcentralus";
    private final String url = "https://westcentralus.api.cognitive.microsoft.com/vision/v2.0/";


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String analyze(String picUrl) {
        String path = "describe";

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key",apiKey);
        String body = "{\"url\":\""+picUrl+"\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<DetectedObjects> response = restTemplate.postForEntity(url + path, request, DetectedObjects.class);
        return response.getBody().toString();
    }
}
