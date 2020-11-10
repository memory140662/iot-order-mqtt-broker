package com.hy.iot.service;

import com.hy.iot.entity.MqttHistory;
import com.hy.iot.repository.MqttHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MqttHistoryService {

    @NonNull private MqttHistoryRepository repository;

    public Page<MqttHistory> getAll(int limit, int offset) {
        val pageable = PageRequest.of(offset, limit);
        return repository.findAll(pageable);
    }

    public boolean isExist(String deviceId, String sensorId, Date time, String topic) {
        return repository.countByDeviceIdAndSensorIdAndTimeAndTopic(deviceId, sensorId, time, topic) > 0;
    }

    public MqttHistory addMqttHistory(MqttHistory mqttHistory) {
        return repository.save(mqttHistory);
    }

}
