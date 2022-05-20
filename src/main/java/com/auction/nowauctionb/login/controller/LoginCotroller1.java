package com.auction.nowauctionb.login.controller;


import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


// 로그인 시 토큰 인증과 권한을 받아야함

@RestController
public class LoginCotroller1 {



    // 기본 로그인도 사용할수 있으니 일단 기본로그인은 남겨두기
    @GetMapping(value = "try-login")
    public String loginTry(){


        return "tryLoginSuccess";
    }


    // get방식으로 토큰값과 유저 이메일을 가져와야함
    // 헤더에 Authentication header이여야함 bearer 넣기

    // 이메일을 체크후 자동 회원가입 및 자동 로그인 해야함
    @GetMapping(value = "try-login-google")
    public String loginTryGoogle(@RequestBody String credentialResponse){

        System.out.println(credentialResponse);



        return credentialResponse;
    }




}
