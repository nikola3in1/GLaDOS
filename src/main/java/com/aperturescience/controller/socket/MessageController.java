package com.aperturescience.controller.socket;

import com.aperturescience.model.messages.WebSocketMessage;
import com.aperturescience.service.serial.SerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private SerialService serialService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Map<String,Object> data(WebSocketMessage message) throws Exception {
        System.out.println(Arrays.toString(message.getData().keySet().toArray()));

        System.out.println(message.getData().get("motors"));

        Map<String, Integer> motors = (Map<String, Integer>) message.getData().get("motors");
        Integer motor1Val = motors.get("motor1");
        String msg = "motor1:" + motor1Val;
//        serialService.sendMsg(msg);
//        String responseMsg = serialService.readMsg();

        Map<String, Object> response = new HashMap<>();
        response.put("success", "true");
//        response.put("recieved", responseMsg);
        return response;
    }
}
