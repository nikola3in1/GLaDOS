package com.aperturescience.controller.rest;

import com.aperturescience.model.messages.WebSocketMessage;
import com.aperturescience.service.state.DataPersistenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data/**")
public class DataPersistenceController {

    private final DataPersistenceService dataPersistenceService;
    private final SimpMessagingTemplate template;

    public DataPersistenceController(DataPersistenceService dataPersistenceService, SimpMessagingTemplate template) {
        this.dataPersistenceService = dataPersistenceService;
        this.template = template;
    }

    @GetMapping("tempAndHum/{temp}/{hum}")
    public ResponseEntity<Map<String, Object>> addTempAndHum(@PathVariable Integer temp, @PathVariable Integer hum) {
        Map<String, Object> response = new HashMap<>();
        dataPersistenceService.insertHumidity(hum);
        dataPersistenceService.insertTemperature(temp);
        return ResponseEntity.ok(response);
    }

    @GetMapping("tempAndHum/{brightness}")
    public ResponseEntity<Map<String, Object>> addBrightness(@PathVariable Integer brightness) {
        Map<String, Object> response = new HashMap<>();
        dataPersistenceService.insertBrightness(brightness);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, List<Integer>>> getAllData() {
        Map<String, List<Integer>> data = dataPersistenceService.getAllData();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/clients")
    public ResponseEntity<WebSocketMessage> updateClients() {
        System.out.println("Sending message");
        updateClientsUtil();
        return ResponseEntity.ok().build();
    }

    private void updateClientsUtil() {
        Map<String, List<Integer>> data = dataPersistenceService.getAllData();

        WebSocketMessage msg = new WebSocketMessage();
        msg.setMessageData(data);
        this.template.convertAndSend("/topic/messages", msg);
    }

}

