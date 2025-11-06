package com.luv2code.springboot.cruddemo.redis.controller;

import com.luv2code.springboot.cruddemo.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisService redisService;

    @GetMapping("/test")
    public String testRedis() {
        try {
            redisService.saveValue("test", "working");
            String result = redisService.getValue("test");
            return "Redis test: " + result;
        } catch (Exception e) {
            return "Redis error: " + e.getMessage();
        }
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisService.getValue(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
        redisService.deleteKey(key);
        return "Deleted";
    }
}
