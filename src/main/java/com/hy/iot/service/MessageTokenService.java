package com.hy.iot.service;

import com.hy.iot.entity.MessageToken;
import com.hy.iot.enums.MessageTokenType;
import com.hy.iot.repository.MessageTokenRepository;
import io.github.cdimascio.japierrors.ApiError;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageTokenService {

    @NonNull private MessageTokenRepository messageTokenRepository;

    public MessageToken registerToken(String token, MessageTokenType type) throws ApiError {
        var messageToken = messageTokenRepository.findMessageTokenByTokenAndType(token, type);
        if (messageToken != null) {
            throw ApiError.badRequest("金鑰重複註冊");
        }
        messageToken = new MessageToken();
        messageToken.setToken(token);
        messageToken.setType(type);
        return messageTokenRepository.save(messageToken);
    }

    public MessageToken removeToke(String token, MessageTokenType type) throws ApiError {
        val messageToken = messageTokenRepository.findMessageTokenByTokenAndType(token, type);
        if (messageToken == null) {
            throw ApiError.notFound("無此金鑰");
        }
        messageTokenRepository.delete(messageToken);
        return messageToken;
    }
}
