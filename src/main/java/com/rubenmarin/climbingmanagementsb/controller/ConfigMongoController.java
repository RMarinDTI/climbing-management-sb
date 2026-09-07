package com.rubenmarin.climbingmanagementsb.controller;

import com.rubenmarin.climbingmanagementsb.service.ConfigMongoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo/config")
public class ConfigMongoController {

    private final ConfigMongoService configMongoService;

    public ConfigMongoController(ConfigMongoService configMongoService) {
        this.configMongoService = configMongoService;
    }

    @GetMapping("/database")
    public String getDatabaseName() {
        return configMongoService.getDatabaseName();
    }

    @GetMapping("/config")
    public String getMongoUri() {
        return configMongoService.getMongoUri();
    }

    @GetMapping("/connection")
    public String getMongoConnection() {
        return configMongoService.getMongoConnectionDetails();
    }

    @GetMapping("/connection-class")
    public String getMongoConnectionDetailsClass() {
        return configMongoService.getMongoConnectionDetailsClass();
    }

    @GetMapping("/mongo-properties")
    public String getMongoProperties() {
        return configMongoService.getMongoProperties();
    }
}