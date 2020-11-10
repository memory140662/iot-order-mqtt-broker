package com.hy.iot.repository;

import com.hy.iot.entity.UserItem;
import com.hy.iot.enums.UserItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserItemRepository extends JpaRepository<UserItem, String> {

    Collection<UserItem> findAllByUser_IdAndStatus(String userId, UserItemStatus status);

}
