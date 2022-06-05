package com.auction.nowauctionb.userAssociated.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController1 {


    @GetMapping(value = "user/info")
    public String getUserName(){


        return "success user Info!!!";
    }

}
