package com.aperturescience.model.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketMessage {
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public void setMessageData(Map<String, List<Integer>> data) {
        Map<String, Object> msgData = new HashMap<>();

        List<List<Integer>> tempAndHum = new ArrayList<>();
        tempAndHum.add(data.get("humidity"));
        tempAndHum.add(data.get("temperature"));

        msgData.put("brightness", data.get("brightness"));
        msgData.put("temperatureAndHumidity", tempAndHum);
        this.setData(msgData);

    }
}
