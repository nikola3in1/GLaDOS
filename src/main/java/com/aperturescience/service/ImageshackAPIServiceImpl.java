package com.aperturescience.service;

import com.aperturescience.model.Image;
import com.aperturescience.model.response.Login;
import com.aperturescience.model.response.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ImageshackAPIServiceImpl implements ImageshackAPIService {

    private final String apiKey = "LCDOWQZ7dda2349e1e76c246c6b613ccad839437";
    private final String url = "https://api.imageshack.com/v2/";

    private final String email = "nikola.3in1@gmail.com";
    private final String password = "glados123";
    private String authToken = "";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String login() {
        String path = "user/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("user", email);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);

        ResponseEntity<Login> response = restTemplate.postForEntity(url + path, request, Login.class);
        this.authToken = response.getBody().getResult().getAuth_token();
        return this.authToken;
    }

    @Override
    public String getAuthToken() {
        return this.authToken;
    }

    @Override
    public String upload(Image image){
        String imageURL = "error";
        String filename = image.getFilename();


        System.out.println(image.getFilename());
        //Params
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("api_key", apiKey );
        map.add("auth_token", authToken);
        try {
            ByteArrayResource contentsAsResource = new ByteArrayResource(image.getStream().toByteArray()) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };
            map.add("file", contentsAsResource);
            Upload response = restTemplate.postForObject(url + "images", map, Upload.class);
            imageURL = response.getResult().getImages()[0].getDirect_link();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://"+imageURL;
    }
}
