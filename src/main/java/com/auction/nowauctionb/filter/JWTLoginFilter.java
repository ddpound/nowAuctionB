package com.auction.nowauctionb.filter;

import com.auction.nowauctionb.configpack.jwtconfig.LoginFilterJWTUtil;
import  com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.service.TokenJoinService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Filter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


// 원래는 /login 을 POST 요청을 통해 username과 password를 담아서 받으면
// UsernamePasswordAuthenticationFilter가 동작하지만 formlogin을 disable했기 때문에
// 작동을 안함 그래서 다시 addFillter해줘야함
// AuthenticationManager를 통해서 로그인을 진행하기 때문에


@Log4j2
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    // RequiredArgsConstructor 가 알아서 생성자에 추가시켜줬기 때문에
    // 로그인 시도를 아래 매니저가 해준다
    private final AuthenticationManager authenticationManager;

    // 스프링 IOC 에서 관리할수 없으니 생성자에서 받아오자
    private final LoginFilterJWTUtil loginFilterJWTUtil;

    // 회원가입을 위한 서비스
   //private TokenJoinService tokenJoinService;

    // 암호화
    //private BCryptPasswordEncoder bCryptPasswordEncoder;


    public JWTLoginFilter(AuthenticationManager authenticationManager, LoginFilterJWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.loginFilterJWTUtil = jwtUtil;
        // login/token 으로 접속하는 모든 링크들을 와일드카드로 이용해서 모두 받아온다
        setFilterProcessesUrl("/login/token/**");
    }

    // 1. username, password를 받아서
    // 2. 정상인지 로그인시도
    // /login 요청을 하면 로그인 시도를 위해서 실행됨
    // 로그인 시도시 PrincipalDetailsService가 호출
    // loadUserByUsername 호출
    // 마지막으로 principalDetails를 세션에 담아주고 JWT 토큰을 만들어서 반환해준다
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {


        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 즉 토큰 요청일때
        if(headerAuth != null){

            try {

                Payload googlepayload = loginFilterJWTUtil.googleVerify(headerAuth.substring("Bearer ".length()));

                // 어쳐피 아래 로그인에서 해줌
                //UserModel userModel = tokenJoinService.findByUsername(googlepayload.getEmail());

                String username = googlepayload.getEmail();
                // 프론트 앤드 .env에도 비밀번호 저장해두고 사용할 예정
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username , loginFilterJWTUtil.getDbsecretkey());

                // principalDetails의 loadUserByUsername() 함수를 찾아 실행함
                // 이게 loadUserByUsername()이 올바르게 작동하고 리턴값이 있다면
                // 올바르게 로그인했다는 뜻
                log.info("logiun try....");
                Authentication authentication =
                        authenticationManager.authenticate(authenticationToken);

                System.out.println("1=====================================1");
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

                System.out.println(principalDetails.getUsername());

                log.info("principalDetails : " +principalDetails.getUserModel().getUsername());

                // 리턴을 올바르게 해주면 세션에 저장됨
                log.info("login success");
                return authentication;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 로그인 신호에 담긴 request를 받아온다
            return null;
        }



        return null;
    }


    // attemptAuthentication 실행후 성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 로그인 시도가 성공했고 인증이 완료 되었다는 뜻입니다.
        log.info("successfulAuthentication");
        log.info("Validation completed, login successful.");


        // authResult가 Principal을 가지고있음
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //토큰 생성
        String makeMyToken = loginFilterJWTUtil.makeAuthToken(principalDetails.getUserModel());

        //헤더에 담아 전송, 프론트에 전달
        response.addHeader("Authorization", "Bearer "+ makeMyToken);
        //super.successfulAuthentication(request, response, chain, authResult);
    }
}
