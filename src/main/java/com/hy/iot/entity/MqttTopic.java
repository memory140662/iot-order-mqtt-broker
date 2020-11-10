package com.hy.iot.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "mqtt_topics")
@Entity
@Data
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MqttTopic implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column
    public String type;

    @Column(name = "is_active")
    public Boolean isActive;
}
