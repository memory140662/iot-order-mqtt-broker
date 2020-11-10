package com.hy.iot.repository;

import com.hy.iot.entity.MessageToken;
import com.hy.iot.enums.MessageTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MessageTokenRepository extends JpaRepository<MessageToken, String> {

    MessageToken findMessageTokenByTokenAndType(String token, MessageTokenType type);

    Collection<MessageToken> findAllByType(MessageTokenType type);
}
