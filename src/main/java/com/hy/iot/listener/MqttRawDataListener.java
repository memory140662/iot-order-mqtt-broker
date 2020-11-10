package com.hy.iot.listener;

import lombok.Data;

import java.util.Date;

public interface MqttRawDataListener {

    boolean onRawDataArrived(RawData rawData);

    @Data
    class RawData {
        private String id;
        private String deviceId;
        private Date time;
        private String[] value;
    }
}
