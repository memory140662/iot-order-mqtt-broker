package com.hy.iot.service;

import com.hy.iot.entity.Item;
import com.hy.iot.entity.User;
import com.hy.iot.entity.UserItem;
import com.hy.iot.enums.UserItemStatus;
import com.hy.iot.enums.UserType;
import com.hy.iot.repository.ItemRepository;
import com.hy.iot.repository.UserItemRepository;
import com.hy.iot.repository.UserRepository;
import io.github.cdimascio.japierrors.ApiError;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final Object lock = new Object();

    @NonNull private ItemRepository itemRepository;

    @NonNull private UserItemRepository userItemRepository;

    @NonNull private UserRepository userRepository;

    public Item addOrder(
            String name,
            String type,
            Integer amount,
            String description,
            Double price
        ) {
        val item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAmount(amount);
        item.setType(type);
        item.setPrice(price);
        return itemRepository.save(item);
    }

    public Page<Item> getAll(int limit, int page) {
        val pageable = PageRequest.of(page, limit);
        return itemRepository.findAll(pageable);
    }

    public UserItem shipment(String orderId) throws ApiError {
        synchronized (lock) {
            val optional = userItemRepository.findById(orderId);
            if (!optional.isPresent()) {
                throw ApiError.notFound("無此訂單記錄");
            }
            val order = optional.get();
            val item = order.getItem();
            if (item.getAmount() - order.getAmount() < 5) {
                item.setAmount(400);
            }

            if (item.getAmount() >= order.getAmount()) {
                item.setAmount(item.getAmount() - order.getAmount());
                order.setStatus(UserItemStatus.SHIPPED);
                itemRepository.save(item);
                userItemRepository.save(order);
            }

            return order;
        }
    }

    public Item update(String itemId, String name, String desc, Integer amount, String type, Double price) throws ApiError {
        val it = itemRepository.findById(itemId);
        if (!it.isPresent()) {
            throw ApiError.notFound("無此商品紀錄");
        }
        if (amount != null) {
            it.get().setAmount(amount);
        }

        if (desc != null) {
            it.get().setDescription(desc);
        }

        if (name != null) {
            it.get().setName(name);
        }

        if (type != null) {
            it.get().setType(type);
        }

        if (price != null) {
            it.get().setPrice(price);
        }

        return itemRepository.save(it.get());
    }

    public Page<UserItem> getAllOrder(int limit, int page ,String userId, String itemId, UserItemStatus status) {
        val matcher = ExampleMatcher.matching().withIgnoreNullValues();
        val userItem = new UserItem();
        val user = new User();
        user.setId(userId);
        userItem.setUser(user);
        val item = new Item();
        item.setId(itemId);
        userItem.setItem(item);
        userItem.setStatus(status);
        val pageable = PageRequest.of(page, limit);
        return userItemRepository.findAll(Example.of(userItem, matcher), pageable);
    }

    public UserItem addOrder(String userId, String itemId, int amount) throws ApiError {
        val user = userRepository.findUserByIdAndType(userId, UserType.CLIENT);
        if (user == null) {
            throw ApiError.notFound("無此顧客紀錄");
        }
        val item = itemRepository.findById(itemId);
        if (!item.isPresent()) {
            throw ApiError.notFound("無此商品紀錄");
        }
        val userItem = new UserItem();
        userItem.setItem(item.get());
        userItem.setUser(user);
        userItem.setAmount(amount);
        userItem.setStatus(UserItemStatus.PENDING);
        return userItemRepository.save(userItem);
    }
}
