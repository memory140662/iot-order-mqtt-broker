package com.hy.iot.service;

import com.hy.iot.config.MqttConfig;
import com.hy.iot.entity.MqttTopic;
import com.hy.iot.repository.MqttTopicRepository;
import io.github.cdimascio.japierrors.ApiError;
import io.swagger.annotations.Api;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class MqttTopicService {

    @NonNull private MqttTopicRepository repository;

    public Page<MqttTopic> findAll(int limit, int page) {
        val example = Example.of(new MqttTopic(), ExampleMatcher.matching().withIgnoreNullValues());
        val pageable = PageRequest.of(page, limit);
        return repository.findAll(example, pageable);
    }

    public Collection<MqttTopic> findAll(MqttTopic mqttTopic) {
        val topic = mqttTopic == null ? new MqttTopic() : mqttTopic;
        val example = Example.of(topic, ExampleMatcher.matching().withIgnoreNullValues());
        return repository.findAll(example);
    }

    public MqttTopic addTopic(String deviceId, String sensorId) throws ApiError {
        var topic = repository.findMqttTopicByDeviceIdAndSensorId(deviceId, sensorId);
        if (topic != null) {
            throw ApiError.badRequest("Topic重複");
        }
        topic = new MqttTopic();
        topic.setIsActive(Boolean.TRUE);
        topic.setDeviceId(deviceId);
        topic.setSensorId(sensorId);
        repository.save(topic);
        MqttConfig.addTopic(deviceId, sensorId);
        return topic;
    }

    public MqttTopic removeTopic(String deviceId, String sensorId) throws ApiError {
        val topic = repository.findMqttTopicByDeviceIdAndSensorId(deviceId, sensorId);
        if (topic == null) {
            throw ApiError.notFound("無此Topic紀錄");
        }
        repository.delete(topic);
        MqttConfig.removeTopic(deviceId, sensorId);
        return topic;
    }



}
