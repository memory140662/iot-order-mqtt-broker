package com.hy.iot.repository;

import com.hy.iot.entity.MqttHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MqttHistoryRepository extends JpaRepository<MqttHistory, String> {

    MqttHistory findByDeviceIdAndSensorIdAndTimeAndTopic(
            String deviceId,
            String sensorId,
            Date time,
            String topic
    );

    int countByDeviceIdAndSensorIdAndTimeAndTopic(
            String deviceId,
            String sensorId,
            Date time,
            String topic
    );
}
