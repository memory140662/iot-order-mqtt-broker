package com.hy.iot.service;

import com.google.firebase.messaging.*;
import com.hy.iot.enums.MessageTokenType;
import com.hy.iot.repository.MessageTokenRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    @NonNull private FirebaseMessaging messaging;

    @NonNull private MessageTokenRepository messageTokenRepository;

    public int pushCheckMessage(String title, String payload, MessageTokenType type) {
        try {
            val messageBuilder = MulticastMessage.builder();
            val tokens = messageTokenRepository.findAllByType(type);
            if (tokens.isEmpty()) {
                return 0;
            }
            tokens.forEach(token -> messageBuilder.addToken(token.getToken()));
            if (StringUtils.isNotBlank(title)) {
                val notification = new Notification("Check!", title);
                messageBuilder.setNotification(notification);
            }
            messageBuilder.putData("payload", payload);
            messageBuilder.putData("type", type.toString());
            val response = messaging.sendMulticast(messageBuilder.build());
            return response.getSuccessCount();
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
