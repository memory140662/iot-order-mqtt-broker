package com.hy.iot.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "mqtt_histories")
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MqttHistory implements Serializable {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column
    private Date time;

    @Column
    private String value;

    @Column
    private String topic;

    @Column(name = "is_succeed")
    private Boolean isSucceed;

    @Column
    private String reason;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

}
