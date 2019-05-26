package com.aperturescience.service.apis;

import com.aperturescience.model.response.DetectedObjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.objectdetection.key}")
    private String apiKey;
    //    private final String region = "westcentralus";
    @Value("${api.objectdetection.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String analyze(String picUrl) {
        String path = "describe";

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key",apiKey);
        String body = "{\"url\":\""+picUrl+"\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<DetectedObjects> response = restTemplate.postForEntity(url + path, request, DetectedObjects.class);
        return response.getBody().toString();
    }
}
