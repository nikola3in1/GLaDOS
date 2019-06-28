package com.aperturescience.service.state;

import com.aperturescience.model.messages.WebSocketMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SocketCommunicationServiceImpl implements SocketCommunicationService {

    private final DataPersistenceService persistenceService;
    private final SimpMessagingTemplate template;

    public SocketCommunicationServiceImpl(DataPersistenceService persistenceService, SimpMessagingTemplate template) {
        this.persistenceService = persistenceService;
        this.template = template;
    }

    @Override
    public void sendCurrentData() {
        Integer currentTemp = persistenceService.getCurrentTemperature();
        Integer currentHum = persistenceService.getCurrentHumidity();

        Map<String, Object> data = new HashMap<>();
        WebSocketMessage msg = new WebSocketMessage();
        data.put("currentTemperature", currentTemp);
        data.put("currentHumidity", currentHum);
        msg.setData(data);
        send(msg);
    }

    @Override
    public void sendAllData() {
        Map<String, List<Integer>> data = persistenceService.getAllData();
        WebSocketMessage msg = new WebSocketMessage();
        msg.setMessageData(data);
        send(msg);
    }

    private void send(WebSocketMessage msg) {
        // Sending msg to topic
        this.template.convertAndSend("/topic/messages", msg);
    }
}
