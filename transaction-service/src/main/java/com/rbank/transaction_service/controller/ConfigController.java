package com.rbank.transaction_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${message: Default message not found}")  // Fallback if config is missing
    private String message;

    @GetMapping("/message")
    public String getMessage() {
        return "Config Message: " + message;
    }
}

