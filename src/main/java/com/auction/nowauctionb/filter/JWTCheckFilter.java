package com.auction.nowauctionb.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// 원래는 /login 을 POST 요청을 통해 username과 password를 담아서 받으면
// UsernamePasswordAuthenticationFilter가 동작하지만 formlogin을 disable했기 때문에
// 작동을 안함 그래서 다시 addFillter해줘야함
// AuthenticationManager를 통해서 로그인을 진행하기 때문에

@RequiredArgsConstructor
public class JWTCheckFilter extends UsernamePasswordAuthenticationFilter {

    // RequiredArgsConstructor 가 알아서 생성자에 추가시켜줬기 때문에
    // 로그인 시도를 아래 매니저가 해준다
    private final AuthenticationManager authenticationManager;


    // 1. username, password를 받아서
    // 2. 정상인지 로그인시도
    // /login 요청을 하면 로그인 시도를 위해서 실행됨
    // 로그인 시도시 PrincipalDetailsService가 호출
    // loadUserByUsername 호출
    // 마지막으로 principalDetails를 세션에 담아주고 JWT 토큰을 만들어서 반환해준다
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {





        return super.attemptAuthentication(request, response);
    }
}
