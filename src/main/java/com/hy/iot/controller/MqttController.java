package com.hy.iot.controller;

import com.hy.iot.entity.MqttHistory;
import com.hy.iot.entity.MqttTopic;
import com.hy.iot.service.MqttHistoryService;
import com.hy.iot.service.MqttTopicService;
import io.github.cdimascio.japierrors.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mqtt")
@RequiredArgsConstructor
@Validated
@Api(tags = {"Mqtt"}, description = "Mqtt的相關操作")
public class MqttController {

    @NonNull private MqttTopicService mqttTopicService;

    @NonNull private MqttHistoryService mqttHistoryService;

    @GetMapping("/topic")
    @ApiOperation(value = "topic 列表")
    public Page<MqttTopic> getAllTopic(
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        return mqttTopicService.findAll(limit, page);
    }

    @PostMapping("/topic")
    @ApiOperation(value = "新增 Mqtt Topic")
    public MqttTopic addTopic(
            @ApiParam(value = "設備ID", required = true)
            @RequestParam(name = "device_id") String deviceId,
            @ApiParam(value = "Sensor ID", required = true)
            @RequestParam(name = "sensor_id") String sensorId
    ) throws ApiError {
        return mqttTopicService.addTopic(deviceId, sensorId);
    }

    @DeleteMapping("/topic/{device_id}/{sensor_id}")
    @ApiOperation(value = "刪除 Mqtt Topic")
    public MqttTopic removeTopic(
            @ApiParam(value = "設備ID", required = true)
            @PathVariable(name = "device_id") String deviceId,
            @ApiParam(value = "Sensor ID", required = true)
            @PathVariable(name = "sensor_id") String sensorId
    ) throws ApiError {
        return mqttTopicService.removeTopic(deviceId, sensorId);
    }

    @GetMapping("/history")
    @ApiOperation(value = "新增 Mqtt 紀錄")
    public Page<MqttHistory> getAllHistory(
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        return mqttHistoryService.getAll(limit, offset);
    }
}
