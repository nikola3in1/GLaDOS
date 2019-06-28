package com.aperturescience.model.messages;

import com.aperturescience.service.serial.SerialService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataSocketMessage implements Serializable {
    private List<Integer> brightness = new ArrayList<>();
    private ArrayList[] temperatureAndHumidity = new ArrayList[2];

    public DataSocketMessage(List<Integer> brightness, List<Integer> temperature, List<Integer> humidity) {
        this.brightness = brightness;
        this.temperatureAndHumidity[0] = (ArrayList) humidity;
        this.temperatureAndHumidity[1] = (ArrayList) temperature;
    }
}
