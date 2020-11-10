package com.hy.iot.controller;

import io.github.cdimascio.japierrors.ApiError;
import lombok.val;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ExceptionController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ApiError handle(HttpServletRequest req, HttpServletResponse res) {
        val status = Integer.parseInt(req.getAttribute("javax.servlet.error.status_code").toString());
        val message = req.getAttribute("javax.servlet.error.message").toString();
        val requestUri = req.getAttribute("javax.servlet.error.request_uri").toString();
        if (status >= 500) return ApiError.internalServerError(message);
        res.setStatus(404);
        return ApiError.notFound(requestUri);
    }

}
