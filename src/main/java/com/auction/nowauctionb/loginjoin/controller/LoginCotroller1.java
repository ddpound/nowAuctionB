package com.auction.nowauctionb.loginjoin.controller;


import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;


// 로그인 시 토큰 인증과 권한을 받아야함

@RestController
public class LoginCotroller1 {

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    UserModelRepository userModelRepository;




    // 기본 로그인도 사용할수 있으니 일단 기본로그인은 남겨두기
    @GetMapping(value = "try-login")
    public String loginTry(){


        return "tryLoginSuccess";
    }


    // get방식으로 토큰값과 유저 이메일을 가져와야함
    // 헤더에 Authentication header이여야함 bearer 넣기

    // 이메일을 체크후 자동 회원가입 및 자동 로그인 해야함
    @GetMapping(value = "try-login-google")
    public String loginTryGoogle(ServletRequest servletRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String headerAuth = req.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("컨트롤러 검증");
        System.out.println(headerAuth);

        DecodedJWT verify = jwtUtil.onlyDecode(headerAuth.substring("Bearer ".length()));

        Base64 base64 = new Base64();
        String decodedString = new String(base64.decode(verify
                .getPayload()
                .getBytes(StandardCharsets.UTF_8)));

        Map<String, String> testMapper = objectMapper.readValue(decodedString, new TypeReference<>(){
        });

        // 가만보니 "이름 성" 이렇게 공백으로 나오게함
        // 이름은 SJ  given_name
        //  성은 Y family_name
        // 그리고 그냥 쓸 닉네임은 name
        //System.out.println(testMapper);
        System.out.println(testMapper.get("email"));
        System.out.println(testMapper.get("name"));

        UserModel userModel = userModelRepository.findByEmail(testMapper.get("email"));

        if(userModel != null){
            // 이미 있다면 토큰이 만료돼었는지 확인
            System.out.println("이미 있는 계정입니다. 토큰 체크하겠습니다...");

        }else{
            // 없으면 회원가입 진행
            System.out.println("없는 계정입니다. 자동회원가입 및 로그인, 토큰 전달을 진행합니다.");
            UserModel userModel1 = new UserModel();
            userModel1.setPassword(bCryptPasswordEncoder.encode("12345")); // 비밀번호는 암호화
            userModel1.setRoles("ROLE_USER"); // 항상 기본이 되는 USER
            userModel1.setUsername(testMapper.get("name"));
            userModel1.setEmail(testMapper.get("email"));
            userModel1.setOauthname("Google");
            userModelRepository.save(userModel1);

            System.out.println("회원가입이 완료되었습니다. 토큰이 발행되었습니다.");
            return jwtUtil.makeAuthToken(userModel1);
        }
        // 새로은 토큰 반환해주기
        return "fail make token....";
    }

    @PostMapping(value = "join")
    public String joinController(@RequestBody UserModel userModel){

        //userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        userModel.setRoles("ROLE_USER");

        return "회원가입 이 완료 되었습니다.";
    }



}
