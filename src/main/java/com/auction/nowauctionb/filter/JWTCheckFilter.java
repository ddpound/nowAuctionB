package com.auction.nowauctionb.filter;







import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.configpack.jwtconfig.LoginFilterJWTUtil;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 여기서 내가 발급한 필터를 검증해서 안쪽으로 보내준다


// 시큐리티가 filter중에 BasicAuthenticationFilter라는 것이 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게됨
// 만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탄다.

@Log4j2
public class JWTCheckFilter extends BasicAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    // 스프링 IOC 에서 관리할수 없으니 생성자에서 받아오자
    private final LoginFilterJWTUtil loginFilterJWTUtil;

    private final UserModelRepository userModelRepository;

    public JWTCheckFilter(AuthenticationManager authenticationManager,LoginFilterJWTUtil loginFilterJWTUtil,UserModelRepository userModelRepository){
        super(authenticationManager);
        this.loginFilterJWTUtil = loginFilterJWTUtil;
        this.userModelRepository = userModelRepository;
        this.authenticationManager = authenticationManager;
    }



    // 실직적인 필터가 하는쪽
    // 인증이나 권한이 필요한 요청이 있을때 해당필터를 타게된다
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 응답 두번방지를 위해서 그냥 넘겨버림
        //super.doFilterInternal(request, response, chain);

        log.info("인증이나 권한이 필요한 주소요청이 됨.");

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        //System.out.println("테스트 헤더값체크 : " + jwtHeader);

        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
        }



        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");

        //만약 구글 토큰인지도 체크해야함, 구글토큰일때도 넘겨야함
        // 아래 토큰 검사할때 에러가 발생하면 체인이 안넘어감
        // 구글 토큰이면 사전에 dofilter 으로 넘겨줘야함
        GoogleIdToken.Payload payload = loginFilterJWTUtil.googleVerify(token);
        if(payload !=null){
            // 구글 토큰은 필요가 없으니 넘겨주자
            // 마찬가지로 넘겨주고 끝내버림
            chain.doFilter(request, response);
        }


        String username = loginFilterJWTUtil.myTokenVerify(token).getClaim("username").asString();

        // 정상적으로 서명이 완료되고 반환값이 있을 예정
        if(username != null){
            UserModel userModel =  userModelRepository.findByUsername(username);


            PrincipalDetails principalDetails = new PrincipalDetails(userModel);
            // 사용자 인증 , 강제로 객체생성 , 마지막 인자를 보면 꼭 권한을 알려줘야함
            // Authentication 객체를 생성
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 세션 공간, 강제로 시큐리티 세션에 접근, Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
