package com.auction.nowauctionb.filter;


import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.service.TokenJoinService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


// 원래는 /login 을 POST 요청을 통해 username과 password를 담아서 받으면
// UsernamePasswordAuthenticationFilter가 동작하지만 formlogin을 disable했기 때문에
// 작동을 안함 그래서 다시 addFillter해줘야함
// AuthenticationManager를 통해서 로그인을 진행하기 때문에

@Log4j2
@RequiredArgsConstructor
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    // RequiredArgsConstructor 가 알아서 생성자에 추가시켜줬기 때문에
    // 로그인 시도를 아래 매니저가 해준다
    private final AuthenticationManager authenticationManager;

    // 스프링 IOC 에서 관리할수 없으니 생성자에서 받아오자
    private JWTUtil jwtUtil;

    // 회원가입을 위한 서비스
    private TokenJoinService tokenJoinService;


    public JWTLoginFilter(AuthenticationManager authenticationManager,
                          JWTUtil jwtUtil, TokenJoinService tokenJoinService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenJoinService = tokenJoinService;

        // login/token 으로 접속하는 모든 링크들을 와일드카드로 이용해서 모두 받아온다
        setFilterProcessesUrl("/login/token/**");
    }

    // 1. username, password를 받아서
    // 2. 정상인지 로그인시도
    // /login 요청을 하면 로그인 시도를 위해서 실행됨
    // 로그인 시도시 PrincipalDetailsService가 호출
    // loadUserByUsername 호출
    // 마지막으로 principalDetails를 세션에 담아주고 JWT 토큰을 만들어서 반환해준다
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 즉 토큰 요청일때
        if(headerAuth != null){

            //d디코드 시작
            DecodedJWT verify = jwtUtil.onlyDecode(headerAuth.substring("Bearer ".length()));
            Base64 base64 = new Base64();
            String decodedString = new String(base64.decode(verify
                    .getPayload()
                    .getBytes(StandardCharsets.UTF_8)));



            try {
                Map<String, String> googleTokenMapper = objectMapper.readValue(decodedString, new TypeReference<>(){
                });

                // 구글에서 날라온 토큰인지도 확인해야함

                UserModel userModel = tokenJoinService.findByEmailSer(googleTokenMapper.get("email"));

                if(userModel == null){
                    // 여기서 걸렸다는 건 로그인 시 최초가입이라는 뜻
                    log.info("This account does not exist");
                    UserModel joinUserModel = UserModel.builder()
                            .password(bCryptPasswordEncoder.encode("1234"))
                            .roles("ROLE_USER")
                            .username(googleTokenMapper.get("name"))
                            .email(googleTokenMapper.get("email"))
                            .oauthname("Google").build();

                    tokenJoinService.joinGoogleUser(joinUserModel); // save 임
                    log.info("save new user email: "+ googleTokenMapper.get("email") );
                    userModel = joinUserModel; // 회원가입 시키고 다시 초기화시켜준다 값을 찾을수 있도록

                    // 여기다가 회원 후 로그인 토큰 발급해줘야함
                }else{
                    log.info("already account. token check");
                }
                    // 회원가입 되어있는 유저라면



                // 이미 있는 계정이니 스프링시큐리티 로그인과
                // 원래 토큰값을 체크와 생성을 해보겠다 라는 뜻


                // 프론트 앤드 .env에도 비밀번호 저장해두고 사용할 예정
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userModel.getEmail() , userModel.getPassword());

                // principalDetails의 loadUserByUsername() 함수를 찾아 실행함
                // 이게 loadUserByUsername()이 올바르게 작동하고 리턴값이 있다면
                // 올바르게 로그인했다는 뜻
                log.info("logiun try....");
                Authentication authentication =
                        authenticationManager.authenticate(authenticationToken);

                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                System.out.println("1=====================================1");
                System.out.println(principalDetails.getUsername());

                // 리턴을 올바르게 해주면 세션에 저장됨
                return authentication;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // 로그인 신호에 담긴 request를 받아온다
        return null;
    }
}
