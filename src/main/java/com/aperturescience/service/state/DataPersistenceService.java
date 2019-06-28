package com.aperturescience.service.state;


import com.aperturescience.model.SensorData;

import java.util.List;
import java.util.Map;

public interface DataPersistenceService {

    void insertTemperature(Integer temperature);

    void insertHumidity(Integer humidity);

    void insertBrightness(Integer brightness);

    SensorData getDataForHour(int hour);

    Map<String, List<Integer>> getAllData();

    SensorData getCurrentData();

    List<SensorData> getDataInInterval(int start, int end);

    Integer getCurrentTemperature();
    Integer getCurrentHumidity();
    Integer getCurrentBrigtness();

    void setCurrentTemperature(Integer temperature);
    void setCurrentHumidity(Integer humidity);
    void setCurrentBrigtness(Integer brigtness);
}
