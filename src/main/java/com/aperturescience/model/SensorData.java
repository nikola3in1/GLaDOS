package com.aperturescience.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SensorData {
    private List<Integer> temperature = Collections.synchronizedList(new ArrayList<>());
    private List<Integer> humidity = Collections.synchronizedList(new ArrayList<>());
    private List<Integer> brightness = Collections.synchronizedList(new ArrayList<>());

    public List<Integer> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Integer> temperature) {
        this.temperature = temperature;
    }

    public List<Integer> getHumidity() {
        return humidity;
    }

    public void setHumidity(List<Integer> humidity) {
        this.humidity = humidity;
    }

    public List<Integer> getBrightness() {
        return brightness;
    }

    public void setBrightness(List<Integer> brightness) {
        this.brightness = brightness;
    }

    public synchronized void addTemperature(Integer temperature) {
        this.temperature.add(temperature);
    }

    public synchronized void addHumidity(Integer humidity) {
        this.humidity.add(humidity);
    }

    public synchronized void addBrightness(Integer brightness) {
        this.brightness.add(brightness);
    }
}

