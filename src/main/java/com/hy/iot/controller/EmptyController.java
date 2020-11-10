package com.hy.iot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 為了讓 heroku 被喚醒所建立的接口，無其他作用
 */
@Controller
public class EmptyController {

    @RequestMapping("/empty")
    @ResponseBody
    public boolean empty() {
        return true;
    }

    @GetMapping
    public String defaultPage() {
        return "redirect:/swagger-ui.html#/";
    }

}
