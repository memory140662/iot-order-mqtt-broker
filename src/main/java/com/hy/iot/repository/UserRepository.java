package com.hy.iot.repository;

import com.hy.iot.entity.User;
import com.hy.iot.enums.UserItemStatus;
import com.hy.iot.enums.UserType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET " +
            "u.name = CASE WHEN :#{#source.name} IS NULL THEN u.name ELSE :#{#source.name} END, " +
            "u.code = CASE WHEN :#{#source.code} IS NULL THEN u.code ELSE :#{#source.code} END, " +
            "u.type = CASE WHEN :#{#source.type} IS NULL THEN u.type ELSE :#{#source.type} END, " +
            "u.isChecked = CASE WHEN :#{#source.isChecked} IS NULL THEN u.isChecked ELSE :#{#source.isChecked} END " +
            "WHERE u.id = :#{#source.id}")
    @Transactional
    int update(@Param("source") User source);

    User findUserByCodeAndIsChecked(String code, Boolean isChecked);

    User findUserByCode(String code);

    User findUserByIdAndType(String id, UserType type);

    @Query(value = "SELECT u.* FROM users u " +
            "INNER JOIN user_items ui " +
            "ON ui.user_id = u.id " +
            "WHERE ui.status = :status " +
            "AND u.type = 'CLIENT'",
            nativeQuery = true)
    Page<User> findAllClientByUserItemStatus(@Param("status") String userItemStatus, Pageable pageable);
}
