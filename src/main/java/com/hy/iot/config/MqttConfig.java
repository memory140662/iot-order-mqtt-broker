package com.hy.iot.config;


import com.google.gson.Gson;
import com.hy.iot.entity.MqttHistory;
import com.hy.iot.entity.MqttTopic;
import com.hy.iot.entity.User;
import com.hy.iot.enums.MessageTokenType;
import com.hy.iot.listener.MqttRawDataListener;
import com.hy.iot.service.*;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

//@Configuration
public class MqttConfig implements MqttRawDataListener {

    @Autowired
    private MqttTopicService mqttTopicService;

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private MqttHistoryService mqttHistoryService;

    private MqttRawDataListener mqttRawDataListener = this;

    @Autowired
    private ItemService itemService;

    private static String USERNAME;
    private static String PASSWORD;
    private static String URL;

    private static MqttPahoMessageDrivenChannelAdapter adapter;

    static {
        val env = Dotenv.configure().ignoreIfMissing().load();
        USERNAME = env.get("MQTT_BROKER_PROJECT_KEY");
        PASSWORD = env.get("MQTT_BROKER_PROJECT_KEY");
        URL = String.format("ssl://%s:8883", env.get("MQTT_BROKER_URL"));
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() throws NoSuchAlgorithmException {
        val clientId = RandomStringUtils.randomAlphanumeric(23);

        adapter = new MqttPahoMessageDrivenChannelAdapter(URL, clientId, clientFactory());
        adapter.setCompletionTimeout(10000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        adapter.addTopic();
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            var isSucceed = false;
            String reason = null;

            val topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
            val json = message.getPayload().toString();
            val rawData = new Gson().fromJson(json, MqttRawDataListener.RawData.class);


            if (mqttHistoryService.isExist(rawData.getDeviceId(), rawData.getId(), rawData.getTime(), topic)) {
                reason = "Record Repeated";
            }

            if (StringUtils.isBlank(reason)) {
                if (StringUtils.startsWith(topic, "/v1/device/")) {
                    isSucceed = mqttRawDataListener.onRawDataArrived(rawData);
                    if (!isSucceed) {
                        reason = "User check status is not unchecked";
                    }
                } else {
                    reason = "Topic Unsupported";
                }
            }


            MqttHistory mqttHistory = new MqttHistory();
            mqttHistory.setTime(rawData.getTime());
            mqttHistory.setDeviceId(rawData.getDeviceId());
            mqttHistory.setSensorId(rawData.getId());
            mqttHistory.setValue(json);
            mqttHistory.setTopic(topic);
            mqttHistory.setIsSucceed(isSucceed);
            mqttHistory.setReason(reason);
            mqttHistoryService.addMqttHistory(mqttHistory);

        };
    }


    @Bean
    public DefaultMqttPahoClientFactory clientFactory() throws NoSuchAlgorithmException {
        val clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(connectOptions());
        return clientFactory;
    }

    @Bean
    public MqttConnectOptions connectOptions() throws NoSuchAlgorithmException {
        val options = new MqttConnectOptions();
        options.setSocketFactory(SSLContext.getDefault().getSocketFactory());
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        options.setCleanSession(Boolean.TRUE);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(30);
        options.setAutomaticReconnect(Boolean.TRUE);
        return options;
    }

    private static String getTopic(String deviceId, String sensorId) {
        return String.format("/v1/device/%s/sensor/%s/rawdata", deviceId, sensorId);
    }

    public static void addTopic(String deviceId, String sensorId) {
        new Thread(() -> {
            while (adapter == null) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                adapter.addTopic(getTopic(deviceId, sensorId));
            } catch (MessagingException ignored) {}
        }).start();
    }

    public static void removeTopic(String deviceId, String sensorId) {
        new Thread(() -> {
            while (adapter == null) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                adapter.removeTopic(getTopic(deviceId, sensorId));
            } catch (MessagingException ignored) {}
        }).start();
    }

    @Override
    public boolean onRawDataArrived(RawData rawData) {
        if (rawData.getValue().length == 1) {
            val userCode = rawData.getValue()[0];
            var user = userService.getUserByCode(userCode);
            if (user != null) {
                switch (user.getType()) {
                    case STUDENT:
                        return sendMessageToStudent(user);
                    case CLIENT:
                        return sendMessageToClient(user);
                }
                return true;
            }
        }

        return false;
    }

    private boolean sendMessageToClient(User user) {
        val gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        firebaseService.pushCheckMessage(null, gson.toJson(user), MessageTokenType.SALE);
        return true;
    }

    private boolean sendMessageToStudent(User user) {
        val checkUser = userService.checkByCode(user.getCode());
        if (checkUser == null) {
            return false;
        }
        firebaseService.pushCheckMessage(String.format("%s 已完成點名", checkUser.getName()), new Gson().toJson(checkUser), MessageTokenType.ORDER);
        return true;
    }


    @PostConstruct
    void started() {
        MqttConfig.addTopic("19126903529", "test_1");
        val mqttTopic = new MqttTopic();
        mqttTopic.setIsActive(Boolean.TRUE);
        mqttTopicService.findAll(mqttTopic).forEach(data -> MqttConfig.addTopic(data.getDeviceId(), data.getSensorId()));
    }
}
