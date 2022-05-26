package com.auction.nowauctionb.loginjoin.controller;


import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auction.nowauctionb.loginjoin.service.TokenJoinService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;


// 로그인 시 토큰 인증과 권한을 받아야함

@RequiredArgsConstructor
@RestController
public class LoginCotroller1 {

    private final TokenJoinService tokenJoinService;



    // 기본 로그인도 사용할수 있으니 일단 기본로그인은 남겨두기
    @GetMapping(value = "try-login")
    public String loginTry(){


        return "tryLoginSuccess";
    }


    // get방식으로 토큰값과 유저 이메일을 가져와야함
    // 헤더에 Authentication header이여야함 bearer 넣기

    // 항상 POST 로 시도해야한다고함, 필터에서 한번 걸러서 엔드포인트인 컨트롤러로 올듯
    // 이메일을 체크후 자동 회원가입 및 자동 로그인 해야함
    @PostMapping (value = "login/token/google")
    public String loginTryGoogle(ServletRequest servletRequest) {

        // 처음 로그인할때 exchang 메소드를 이용해서 아이디 검증후 없다면
        // join문 실행해줘도 될듯




        return "good";
    }

    // google token으로 회원가입하겠다는 뜻
    @PostMapping(value = "join/googletoken")
    public String joinController(HttpServletRequest request){

        tokenJoinService.googleTokenJoingetHeader(request);


        return "Your membership registration is complete.";
    }

    @GetMapping(value = "/user/test1")
    public String testAuthoriContriller(){
        return "유저 권한";
    }


}
