package com.aperturescience.service.serial;

public interface SerialService {
    void initSerialPort();
    void nonBlockingReading();
    void sendMsg(String msg);
    void readMsg();
}
