package com.lym.zk.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    public void doBusiness(){
        System.out.println("do business");
    }
}
