package com.hy.iot.repository;

import com.hy.iot.entity.MqttTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MqttTopicRepository extends JpaRepository<MqttTopic, String> {
    MqttTopic findMqttTopicByDeviceIdAndSensorId(String deviceId, String sensorId);
}
