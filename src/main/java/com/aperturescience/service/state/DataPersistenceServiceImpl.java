package com.aperturescience.service.state;

import com.aperturescience.model.MotorStates;
import com.aperturescience.model.SensorData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataPersistenceServiceImpl implements DataPersistenceService {

    // Key: Hour
    // Value: SensorData
    private Map<Integer, SensorData> data = Collections.synchronizedMap(new HashMap<>());
    private AtomicInteger currentTemperature = new AtomicInteger();
    private AtomicInteger currentHumidity = new AtomicInteger();
    private AtomicInteger currentBrightness = new AtomicInteger();
    private MotorStates currentStates = new MotorStates();
    private String lastPicUrl = "";

    public synchronized void insertTemperature(Integer temperature) {
        // Validation
        if (temperature == null || temperature < -200 || temperature > 60) {
            return;
        }

        SensorData currentHourData = getCurrentHourData();
        // Update data

        // If there's yet no data for given hour
        if (currentHourData == null) {
            currentHourData = new SensorData();
            data.put(getCurrentHour(), currentHourData);
        }
        currentHourData.addTemperature(temperature);
        currentTemperature.set(temperature);
    }

    public synchronized void insertHumidity(Integer humidity) {
        // Validation
        if (humidity == null || humidity < 0 || humidity > 100) {
            return;
        }

        SensorData currentHourData = getCurrentHourData();
        // Update data

        // If there's yet no data for given hour
        if (currentHourData == null) {
            currentHourData = new SensorData();
            data.put(getCurrentHour(), currentHourData);
        }
        currentHourData.addHumidity(humidity);
        currentHumidity.set(humidity);
    }

    public synchronized void insertBrightness(Integer brightness) {
        // Validation
        if (brightness < 0 || brightness > 1024) {
            return;
        }

        SensorData currentHourData = getCurrentHourData();

        if (currentHourData == null) {
            currentHourData = new SensorData();
            data.put(getCurrentHour(), currentHourData);
        }
        currentHourData.addBrightness(brightness);
        this.currentBrightness.set(brightness);
    }

    public synchronized SensorData getDataForHour(int hour) {
        return data.get(hour);
    }

    @Override
    public synchronized Map<String, List<Integer>> getAllData() {
        Map<String, List<Integer>> responseData = new HashMap<>();
        List<Integer> brightness = new ArrayList<>();
        List<Integer> temperature = new ArrayList<>();
        List<Integer> humidity = new ArrayList<>();


        System.out.println("CURRENT HOUR: " + getCurrentHour());
        for (int i = 0; i < 24; i++) {
            brightness.add(0);
            temperature.add(0);
            humidity.add(0);
        }

        for (Map.Entry<Integer, SensorData> entry : data.entrySet()) {
            // key is hour
            Integer hour = entry.getKey();
            SensorData dataForGivenHour = entry.getValue();
            Integer avgBrighness = getAverage(dataForGivenHour.getBrightness());
            Integer avgTemperature = getAverage(dataForGivenHour.getTemperature());
            Integer avgHumidity = getAverage(dataForGivenHour.getHumidity());
            brightness.set(hour, avgBrighness);
            temperature.set(hour, avgTemperature);
            humidity.set(hour, avgHumidity);
        }

        responseData.put("humidity", humidity);
        responseData.put("temperature", temperature);
        responseData.put("brightness", brightness);

        return responseData;
    }

    private Integer getAverage(List<Integer> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }
        Integer sum = 0;
        for (Integer nr : dataList) {
            sum += nr;
        }
        return sum / dataList.size();
    }

    public synchronized SensorData getCurrentData() {
        return getCurrentHourData();
    }

    public synchronized List<SensorData> getDataInInterval(int start, int end) {
        List<SensorData> dataInInterval = new ArrayList<>();
        for (int h = start; h <= end; h++) {
            dataInInterval.add(getDataForHour(h));
        }
        return dataInInterval;
    }

    @Override
    public Integer getCurrentTemperature() {
        return currentTemperature.get();
    }

    @Override
    public Integer getCurrentHumidity() {
        return currentHumidity.get();
    }

    @Override
    public Integer getCurrentBrigtness() {
        return currentBrightness.get();
    }

    @Override
    public void setCurrentTemperature(Integer temperature) {
        this.currentTemperature.set(temperature);
    }

    @Override
    public void setCurrentHumidity(Integer humidity) {
        this.currentHumidity.set(humidity);
    }

    @Override
    public void setCurrentBrigtness(Integer brigtness) {
        this.currentBrightness.set(brigtness);
    }

    @Override
    public synchronized void setCurrentStates(MotorStates states) {
        currentStates = new MotorStates(states.getHead(), states.getNeck(), states.getElbow(), states.getBase());
    }

    @Override
    public synchronized MotorStates getCurrentStates() {
        return this.currentStates;
    }

    @Override
    public synchronized void setLastPicUrl(String url) {
        this.lastPicUrl = url;
    }

    @Override
    public synchronized String getLastPicUrl() {
        return lastPicUrl;
    }

    private synchronized SensorData getCurrentHourData() {
        return data.get(getCurrentHour());
    }

    private Integer getCurrentHour() {
        Calendar cal = new GregorianCalendar();
        return cal.get(Calendar.HOUR_OF_DAY) - 1;
    }

    @Scheduled(cron = "1 1 1 * * *")
    synchronized void resetData() {
        data = Collections.synchronizedMap(new HashMap<>());
    }

    // Test
    {
//        System.out.println("CALLED");
//        Random r = new Random();
//        for (int i = 0; i < 24; i++) {
//            SensorData data = new SensorData();
//            data.addBrightness(r.nextInt(30));
//            data.addTemperatureAndHumidity(r.nextInt(30), r.nextInt(30));
//
//            this.data.put(i, data);
//        }
//        System.out.println("DATA SIZE "+data.size());
    }

}
