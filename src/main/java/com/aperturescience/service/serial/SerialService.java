package com.aperturescience.service.serial;

public interface SerialService {
    String sendMsg(String key, Integer value);
    void readMsg();
    String blockingClient();
}
