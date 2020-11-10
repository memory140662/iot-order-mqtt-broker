package com.hy.iot.config;

import io.github.cdimascio.japierrors.ApiError;
import io.github.cdimascio.japierrors.basic.ApiErrorBasic;
import io.github.cdimascio.japierrors.wcp.ApiErrorWcp;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(ApiError.class)
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public ApiError apiError(HttpServletResponse res, ApiError ex) {
        ex.printStackTrace();
        int status = 500;
        if (ex instanceof ApiErrorWcp) {
            status = ((ApiErrorWcp) ex).getStatus().getCode();
        } else if (ex instanceof ApiErrorBasic) {
            status = ((ApiErrorBasic) ex).getCode();
        } else {
            ex.addSuppressed(new Exception("unknown ApiError type"));
        }
        res.setStatus(status);
        return ex;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order
    public ApiError catchAll(Exception ex) {
        ex.printStackTrace();
        return ApiError.internalServerError(ex);
    }

}
