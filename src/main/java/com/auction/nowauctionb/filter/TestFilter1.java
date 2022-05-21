package com.auction.nowauctionb.filter;


import com.auction.nowauctionb.configpack.JWTUtil;
import com.auction.nowauctionb.configpack.VerifyResult;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


// 아래 필터 체인은 시큐리티 필터가 아니니깐 등록이 불가
// addFilterBefore, addFilterAfter 를 통해서
// 시큐리티 필터가 작동하기 전이나 후에 돌도록 설정해줘야함

@Component
public class TestFilter1 implements Filter {

    @Autowired
    JWTUtil jwtUtil = new JWTUtil();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        System.out.println(req);

        String headerAuth = req.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("테스트 필터1");
        System.out.println(headerAuth);
        DecodedJWT verify = jwtUtil.verify(headerAuth.substring("Bearer ".length()));

        Base64 base64 = new Base64();
        String decodedString = new String(base64.decode(verify
                        .getPayload()
                        .getBytes(StandardCharsets.UTF_8)));

        System.out.println(decodedString);
        // 필터 체인 두 필터를 통해 반드시 등록해줘야한다
        // 프로세스를 꼭 마저 진행해라
        filterChain.doFilter(req, res);
    }
}
