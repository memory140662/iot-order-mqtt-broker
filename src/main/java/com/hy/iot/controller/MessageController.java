package com.hy.iot.controller;

import com.hy.iot.entity.MessageToken;
import com.hy.iot.enums.MessageTokenType;
import com.hy.iot.service.MessageTokenService;
import io.github.cdimascio.japierrors.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
@Api(tags = {"手機推播Message"})
@Validated
public class MessageController {

    @NonNull private MessageTokenService messageTokenService;

    @PostMapping("/sub/order")
    @ApiOperation(value = "註冊點名推送功能")
    public MessageToken subOrderMessage(
            @ApiParam(value = "Firebase 金鑰", required = true)
            @RequestParam String token
    ) throws ApiError {
        return messageTokenService.registerToken(token, MessageTokenType.ORDER);
    }

    @DeleteMapping("/unsub/order/{token}")
    @ApiOperation(value = "取消點名推送功能")
    public boolean unsubOrderMessage(
            @ApiParam(required = true, value = "Firebase 金鑰")
            @PathVariable String token
    ) throws ApiError {
        return messageTokenService.removeToke(token, MessageTokenType.ORDER) != null;
    }


    @PostMapping("/sub/sale")
    @ApiOperation(value = "註冊購物推送功能")
    public MessageToken subSaleMessage(
            @ApiParam(value = "Firebase 金鑰", required = true)
            @RequestParam String token
    ) throws ApiError {
        return messageTokenService.registerToken(token, MessageTokenType.SALE);
    }

    @DeleteMapping("/unsub/sale/{token}")
    @ApiOperation(value = "取消購物推送功能")
    public boolean unsubSaleMessage(
            @ApiParam(required = true, value = "Firebase 金鑰")
            @PathVariable String token
    ) throws ApiError {
        return messageTokenService.removeToke(token, MessageTokenType.SALE) != null;
    }
}
