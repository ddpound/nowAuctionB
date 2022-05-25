package com.auction.nowauctionb.filter;


import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


// 아래 필터 체인은 시큐리티 필터가 아니니깐 등록이 불가
// addFilterBefore, addFilterAfter 를 통해서
// 시큐리티 필터가 작동하기 전이나 후에 돌도록 설정해줘야함

// 또한 기본적으로 필터는 스프링 컨텍스트가 관리하는게 아니게 아니라서
// 의존성 주입을 할 수 없습니다.
@Component
public class TestFilter1 implements Filter {
    // 이미 componenet로 등록했다면 객체에 new를 선언해서 다시 선언하면 안됨
    // 필터는 필터에 추가할때 필요한 클래스들을 넣어줘야합니다.
    JWTUtil jwtUtil;
    BCryptPasswordEncoder bCryptPasswordEncoder;



    public TestFilter1(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();


        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        System.out.println(req);

        String headerAuth = req.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("테스트 필터1");
        System.out.println(headerAuth.substring("Bearer ".length()));
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



        // decodedString 이걸 가지고 값을 찾아내자
        //System.out.println(decodedString);
        // 필터 체인 두 필터를 통해 반드시 등록해줘야한다
        // 프로세스를 꼭 마저 진행해라
        filterChain.doFilter(req, res);
    }
}
