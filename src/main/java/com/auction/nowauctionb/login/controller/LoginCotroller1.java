package com.auction.nowauctionb.login.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// 로그인 시 토큰 인증과 권한을 받아야함

@RestController
public class LoginCotroller1 {


    

    @GetMapping(value = "try-login")
    public String loginTry(){


        return "tryLoginSuccess";
    }




}
