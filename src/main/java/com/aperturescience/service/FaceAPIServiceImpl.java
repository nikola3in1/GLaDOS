package com.aperturescience.service;

import com.aperturescience.model.response.DetectedObjects;
import com.aperturescience.model.response.FaceData;
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
public class FaceAPIServiceImpl implements FaceAPIService {
    private final String apiKey = "c999bb06b2f047aca34ceca80f1ad81a";
    private final String url = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public FaceData analyze(String picUrl) {
        String path = "detect";

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key",apiKey);
        String body = "{\"url\":\""+picUrl+"\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<FaceData[]> response = restTemplate.postForEntity(url + path, request, FaceData[].class);
        return response.getBody()[0];
    }
}
