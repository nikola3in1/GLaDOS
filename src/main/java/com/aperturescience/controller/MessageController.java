package com.aperturescience.controller;

import com.aperturescience.model.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Map<String,Object> data(WebSocketMessage message) throws Exception {
        System.out.println(Arrays.toString(message.getData().keySet().toArray()));
        Map<String, Object> response = new HashMap<>();
        response.put("success", "true");
        response.put("recieved", message);
        return response;
    }
}
