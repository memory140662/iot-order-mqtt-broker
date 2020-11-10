package com.hy.iot.controller;

import com.hy.iot.entity.User;
import com.hy.iot.enums.UserItemStatus;
import com.hy.iot.enums.UserType;
import com.hy.iot.service.UserService;
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

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Api(description = "可進行使用者的相關操作(例如：獲取學生列表，查詢等等)", tags = {"使用者"})
public class UserController {

    @NonNull private UserService userService;

    @GetMapping("/student")
    @ApiOperation(value = "獲取學生列表")
    public Page<User> getAllStudent(
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int page,
            @ApiParam(value = "是否點名")
            @RequestParam(required = false, name = "is_checked") Boolean isChecked
    ) {
        return userService.getAll(limit, page, UserType.STUDENT, isChecked, null);
    }

    @GetMapping("/client")
    @ApiOperation(value = "獲取顧客列表")
    public Page<User> getAllClient(
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int page,
            @ApiParam(value = "訂單狀態")
            @RequestParam(required = false) UserItemStatus status
            ) {
        return userService.getAll(limit, page, UserType.CLIENT, null, status);
    }

    @PostMapping("/student")
    @ApiOperation(value = "新增學生")
    public User addStudent(
            @ApiParam(value = "學生名稱", required = true)
            @RequestParam String name,
            @ApiParam(value = "學生編碼", required = true)
            @RequestParam String code,
            @ApiParam(value = "學生班級", required = true)
            @RequestParam(name = "class") String clx,
            @ApiParam(value = "學生年齡", required = true)
            @Min(0)
            @RequestParam Integer age
    ) throws ApiError {
        return userService.addUser(name, code, UserType.STUDENT, clx, age);
    }

    @PutMapping("/student/{id}")
    @ApiOperation(value = "修改學生")
    public User updateStudent(
            @ApiParam(value = "學生ID", required = true)
            @PathVariable String id,
            @ApiParam(value = "學生名稱")
            @RequestParam(required = false) String name,
            @ApiParam(value = "學生編碼")
            @RequestParam(required = false) String code,
            @ApiParam(value = "學生班級")
            @RequestParam(name = "class", required = false) String clx,
            @ApiParam(value = "學生年齡")
            @Min(0)
            @RequestParam(required = false) Integer age
    ) throws ApiError {
        return userService.updateUser(id, name, code, age, clx);
    }

    @PutMapping("/clean/check")
    @ApiOperation(value = "將點名狀態清除為false")
    public int cleanCheck() {
        return userService.cleanCheck();
    }

    @PostMapping("/client")
    @ApiOperation(value = "新增顧客")
    public User addClient(
            @ApiParam(value = "顧客名稱", required = true)
            @RequestParam String name,
            @ApiParam(value = "顧客編碼", required = true)
            @RequestParam String code,
            @ApiParam(value = "顧客年齡", required = true)
            @Min(0)
            @RequestParam Integer age
    ) throws ApiError {
        return userService.addUser(name, code, UserType.CLIENT, null, age);
    }


    @PutMapping("/client/{id}")
    @ApiOperation(value = "新增顧客")
    public User updateClient(
            @ApiParam(value = "顧客ID", required = true)
            @PathVariable String id,
            @ApiParam(value = "顧客名稱")
            @RequestParam(required = false) String name,
            @ApiParam(value = "顧客編碼")
            @RequestParam(required = false) String code,
            @ApiParam(value = "顧客年齡")
            @Min(0)
            @RequestParam(required = false) Integer age
    ) throws ApiError {
        return userService.updateUser(id, name, code, age, null);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "刪除學生/顧客")
    public User delete(
            @ApiParam(value = "學生/顧客ID", required = true)
            @PathVariable String id
    ) throws ApiError {
        return userService.deleteUser(id);
    }
}
