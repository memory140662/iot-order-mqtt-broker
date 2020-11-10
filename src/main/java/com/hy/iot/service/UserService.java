package com.hy.iot.service;


import com.hy.iot.entity.CheckHistory;
import com.hy.iot.entity.User;
import com.hy.iot.enums.UserItemStatus;
import com.hy.iot.enums.UserType;
import com.hy.iot.repository.CheckHistoryRepository;
import com.hy.iot.repository.UserRepository;
import io.github.cdimascio.japierrors.ApiError;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @NonNull private UserRepository userRepository;

    @NonNull private CheckHistoryRepository checkHistoryRepository;

    public User checkByCode(String code) {
        var user = userRepository.findUserByCodeAndIsChecked(code, false);

        if (user == null) return null;

        user.setIsChecked(Boolean.TRUE);
        user = userRepository.saveAndFlush(user);

        val history = new CheckHistory();
        history.setUser(user);
        checkHistoryRepository.save(history);

        return user;
    }

    @Transactional(readOnly = true)
    public Page<User> getAll(int limit, int page, UserType type, Boolean isChecked, UserItemStatus status) {
        val user = new User();
        val pageable = PageRequest.of(page, limit);
        val matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreNullValues();
        if (type != null) {
            user.setType(type);
        }

        if (isChecked != null) {
            user.setIsChecked(isChecked);
        }

        if (status != null && type == UserType.CLIENT) {
            return userRepository.findAllClientByUserItemStatus(status.toString(), pageable);
        }


        return userRepository.findAll(Example.of(user, matcher), pageable);
    }

    public User addUser(String name, String code, UserType type, String clx, Integer age) throws ApiError {
        var user = userRepository.findUserByCode(code);
        if (user != null) {
            throw ApiError.badRequest("使用者編碼不可重複");
        }
        user = new User(code, name, type, Boolean.FALSE);
        user.setClx(clx);
        user.setAge(age);
        return userRepository.save(user);
    }

    public int cleanCheck() {
        val user = new User();
        user.setIsChecked(Boolean.TRUE);
        user.setType(UserType.STUDENT);
        val example = Example.of(user, ExampleMatcher.matchingAll().withIgnoreNullValues());
        val users = userRepository.findAll(example);
        System.out.println(users);
        users.forEach(u -> u.setIsChecked(Boolean.FALSE));
        userRepository.saveAll(users);
        return users.size();
    }

    public User updateUser(String id, String name, String code, Integer age, String clx) throws ApiError {
        val optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw ApiError.notFound("無此學生/顧客紀錄");
        }

        val user = optional.get();

        if (name != null) {
            user.setName(name);
        }

        if (code != null) {
            user.setCode(code);
        }

        if (age != null) {
            user.setAge(age);
        }

        if (clx != null) {
            user.setClx(clx);
        }

        return userRepository.save(user);
    }

    public User getUserByCode(String code) {
        return userRepository.findUserByCode(code);
    }


    public User deleteUser(String id) throws ApiError {
        val optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw ApiError.notFound("無此使用者紀錄");
        }
        val user = optional.get();
        userRepository.delete(user);
        return user;
    }

}
