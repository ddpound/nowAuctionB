package com.auction.nowauctionb.loginjoin.controller;


import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;


import com.auction.nowauctionb.configpack.jwtconfig.service.JwtSuperintendService;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.service.TokenJoinService;

import com.auction.nowauctionb.userAssociated.frontmodel.UserModelFront;
import com.auction.nowauctionb.userAssociated.service.UserService1;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;


// 로그인 시 토큰 인증과 권한을 받아야함

@Log4j2
@RequiredArgsConstructor
@RestController
public class LoginCotroller1 {

    // JWT 토큰 디비 추척을 위한 서비스
    private final JwtSuperintendService jwtSuperintendService;

    private final UserService1 userService1;

    private final TokenJoinService tokenJoinService;

    private final JWTUtil jwtUtil;




    // 기본 로그인도 사용할수 있으니 일단 기본로그인은 남겨두기
    @GetMapping(value = "try-login")
    public String loginTry(){


        return "tryLoginSuccess";
    }


    // get방식으로 토큰값과 유저 이메일을 가져와야함
    // 헤더에 Authentication header이여야함 bearer 넣기

    // get 으로 실행해도 작동함 토큰안에 값이있기 때문에,
    // 필터에서 한번 걸러서 엔드포인트인 컨트롤러로 올듯
    // 이메일을 체크후 자동 회원가입 및 자동 로그인 해야함
    @GetMapping (value = "login/token/google")
    public UserModelFront loginTryGoogle(Authentication authentication , HttpServletResponse response) {

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            String makeMyToken = jwtUtil.makeAuthToken(principalDetails.getUserModel());
            String makeRefleshToken = jwtUtil.makeRfreshToken(principalDetails.getUserModel());


            //log.info("make Token : "+makeMyToken);
            //헤더에 담아 전송, 프론트에 전달
            response.addHeader("Authorization", "Bearer "+ makeMyToken);
            response.addHeader("RefreshToken","Bearer "+ makeRefleshToken);


            jwtSuperintendService.saveCheckTokenRepository(principalDetails.getUsername(),makeMyToken,makeRefleshToken);

            return userService1.findUserNameFrontUserModel(principalDetails.getUsername());
            //return "seuccess";
        }catch (NullPointerException e){
            log.info("principalDetails is null, LoginController");
            return null;
        }
        // 처음 로그인할때 exchang 메소드를 이용해서 아이디 검증후 없다면
        // join문 실행해줘도 될듯

    }

    // google token으로 회원가입하겠다는 뜻
    @PostMapping(value = "join/googletoken")
    public String joinController(HttpServletRequest request){




        tokenJoinService.googleTokenJoingetHeader(request);


        return "Your membership registration is complete.";
    }





}
