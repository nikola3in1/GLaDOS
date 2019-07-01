package com.aperturescience.config;

import com.aperturescience.service.serial.SerialService;
import com.aperturescience.service.serial.SerialServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitBean {
    private final SerialService serialService;

    public InitBean(SerialService serialService) {
        this.serialService = serialService;
    }

    @PostConstruct
    public void init() {
        System.out.println("Serial reading service is running");
        serialService.initSerialPort();
        new Thread(serialService::readMsg).start();
//        serialService.sendMsg(SerialServiceImpl.Messages.setIdle);
    }

}
