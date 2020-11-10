package com.hy.iot.controller;

import com.hy.iot.entity.Item;
import com.hy.iot.entity.User;
import com.hy.iot.entity.UserItem;
import com.hy.iot.enums.UserItemStatus;
import com.hy.iot.service.ItemService;
import io.github.cdimascio.japierrors.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Validated
@Api(tags = {"銷售物品"}, description = "可進行物品的相關操作")
public class ItemController {

    @NonNull private ItemService itemService;

    @PostMapping
    @ApiOperation(value = "新增銷售物品")
    public Item addItem(
            @ApiParam(value = "物品名稱", required = true)
            @RequestParam String name,
            @ApiParam(value = "物品類型", required = true)
            @RequestParam String type,
            @ApiParam(value = "物品描述")
            @RequestParam(required = false) String description,
            @ApiParam(value = "庫存數量")
            @Min(0)
            @RequestParam(defaultValue = "0", required = false) Integer amount,
            @ApiParam(value = "物品價格")
            @Min(0)
            @RequestParam(defaultValue = "0", required = false) Double price
    ) {
        return itemService.addOrder(name, type, amount, description, price);
    }

    @GetMapping
    @ApiOperation(value = "銷售物品列表")
    public Page<Item> getAll(
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        return itemService.getAll(limit, page);
    }


    @PutMapping("/shipment/{order_id}")
    @ApiOperation(value = "出貨")
    public UserItem Shipment(
            @ApiParam(value = "訂單ID", required = true)
            @PathVariable("order_id")
            String orderId
    ) throws ApiError {
        return itemService.shipment(orderId);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改庫存物品")
    public Item update(
            @ApiParam(value = "物品ID", required = true)
            @PathVariable
            String id,
            @ApiParam(value = "物品名稱")
            @RequestParam(required = false) String name,
            @ApiParam(value = "物品類型")
            @RequestParam(required = false) String type,
            @ApiParam(value = "物品描述")
            @RequestParam(required = false) String description,
            @ApiParam(value = "庫存數量")
            @Min(0)
            @RequestParam(required = false) Integer amount,
            @ApiParam(value = "物品價格")
            @Min(0)
            @RequestParam(required = false) Double price
    ) throws ApiError {
        return itemService.update(id, name, description, amount, type, price);
    }

    @GetMapping("/order")
    @ApiOperation(value = "訂單")
    public Page<UserItem> orders(
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int page,
            @ApiParam(value = "使用者ID")
            @RequestParam(name = "user_id", required = false)
            String userId,
            @ApiParam(value = "物品ID")
            @RequestParam(name = "item_id", required = false)
            String itemId,
            @ApiParam(value = "訂單狀態 (PENDING: 準備中; SHIPPED: 已出貨; CANCEL: 取消)")
            @RequestParam(required = false)
            UserItemStatus status
    ) {
        return itemService.getAllOrder(limit, page, userId, itemId, status);
    }

    @PostMapping("/order")
    @ApiOperation(value = "新增訂單")
    public UserItem addOrder(
            @ApiParam(value = "使用者ID", required = true)
            @RequestParam(name = "user_id") String userId,
            @ApiParam(value = "物品ID", required = true)
            @RequestParam(name = "item_id") String itemId,
            @ApiParam(value = "數量", required = true)
            @Min(1)
            @RequestParam(defaultValue = "1") int amount
    ) throws ApiError {
        return itemService.addOrder(userId, itemId, amount);
    }
}
