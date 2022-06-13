package com.auction.nowauctionb.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {


    @GetMapping
    public String setAdminUrl(){

        return "successAdmin";
    }


}
