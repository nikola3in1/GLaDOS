package com.aperturescience.service.apis;

import com.aperturescience.model.Image;
import com.aperturescience.model.response.Login;
import com.aperturescience.model.response.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.imageshack.key}")
    private String apiKey;
    @Value("${api.imageshack.url}")
    private String url;
    @Value("${api.imageshack.email}")
    private String email;
    @Value("${api.imageshack.password}")
    private String password;

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
