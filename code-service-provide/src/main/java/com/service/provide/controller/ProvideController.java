package com.service.provide.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "rpc")
public class ProvideController {

    @GetMapping
    public String getRpcResult(@RequestParam(value = "name" )String name){
        return "hello " + name;
    }
}
