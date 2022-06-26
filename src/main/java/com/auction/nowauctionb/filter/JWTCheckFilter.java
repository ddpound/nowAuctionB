package com.auction.nowauctionb.filter;







import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.configpack.jwtconfig.LoginFilterJWTUtil;
import com.auction.nowauctionb.configpack.jwtconfig.repository.JwtSuperintendRepository;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


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

    private final JwtSuperintendRepository jwtSuperintendRepository;




    public JWTCheckFilter(AuthenticationManager authenticationManager,
                          LoginFilterJWTUtil loginFilterJWTUtil,
                          UserModelRepository userModelRepository,
                          JwtSuperintendRepository jwtSuperintendRepository){
        super(authenticationManager);
        this.loginFilterJWTUtil = loginFilterJWTUtil;
        this.userModelRepository = userModelRepository;
        this.authenticationManager = authenticationManager;
        this.jwtSuperintendRepository = jwtSuperintendRepository;
    }



    // 실직적인 필터가 하는쪽
    // 인증이나 권한이 필요한 요청이 있을때 해당필터를 타게된다
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 응답 두번방지를 위해서 ,안그럼 그냥 넘겨버림
        //super.doFilterInternal(request, response, chain);

        log.info("JWTCheckFilter has been activated.");

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String reFreshJwtHeader = request.getHeader("RefreshToken");


        //System.out.println("테스트 헤더값체크 : " + jwtHeader);
        // 늘 리프레시와 액세스를 동시에 받아야함
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")
                || reFreshJwtHeader == null || !reFreshJwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
        }


        // 여기 받아온것도 DB에 체크해봐야함
        String token = jwtHeader.replace("Bearer ", "");


        //만약 구글 토큰인지도 체크해야함, 구글토큰일때도 넘겨야함
        // 아래 토큰 검사할때 에러가 발생하면 체인이 안넘어감
        // 구글 토큰이면 사전에 dofilter 으로 넘겨줘야함
        GoogleIdToken.Payload payload = loginFilterJWTUtil.googleVerify(token);
        if(payload != null){
            // 구글 토큰은 필요가 없으니 넘겨주자
            // 마찬가지로 넘겨주고 끝내버림
            chain.doFilter(request, response);
        }
        // 아주중요함 두 체인필터를 타고 다시 돌아온다.
        // 이걸 왜 체인끝에 두는 지알겠음
        // 체인 -> 컨트롤러 -> 다시체인(즉 나갈때)이순인데
        // dofilter해서 컨트롤러를 보낸 다음에 여기로 다시옴;;

        // 구글 토큰 이라면 리프레시 토큰은 없음 그리고 일반 토큰이라면 있으니 여기배치
        String reFreshtoken = null;

        if(reFreshJwtHeader != null){
            reFreshtoken = reFreshJwtHeader.replace("Bearer ", "");
        }


        // 1일때 검증완료, -2 면 토큰 만료
        // 이렇게 담아두면 다시 재 검증할 필요가 없음
        Map<Integer, DecodedJWT> resultMapToken = loginFilterJWTUtil.returnMapMyTokenVerify(token);

        try {

            String username;

            // -2 즉 만료일 때
            if(resultMapToken.containsKey(-2)){
                // 리프레시 토큰 검증 시작, 값 변경
                resultMapToken = loginFilterJWTUtil.returnMapMyTokenVerify(reFreshtoken);

            }


            // 즉 1 인 key값이 있는지 체크,
            if(resultMapToken.containsKey(1)){

                // 리프레쉬 과정중에 에러발생함 찾아내기, 운동하고찾아내기

                // 리프레시 토큰이라면 이게 있을것이다
                if(resultMapToken.get(1).getClaim("refresh").asString() != null){

                    // 리프레시 토큰, 액세스 토큰 다 DB검색
                    // 디비에 한쌍으로 검색, 만약 없다면 누가 탈취해서 임의로 값을 넣은걸 의심
                    UserModel userModel = jwtSuperintendRepository.findByAccessTokenAndRefreshToken(token,reFreshtoken).getUser();

                    String newAccessToken = loginFilterJWTUtil.makeAuthToken(userModel);

                    //리프레시까지 다시 재발급이아니라 액세스만 재발급해서 다시 DB한쌍에 저장
                    //String newRefreshToken = loginFilterJWTUtil.makeRfreshToken(userModel);

                    response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ newAccessToken);
                    response.addHeader("RefreshToken","Bearer "+ reFreshtoken);

                    jwtSuperintendRepository
                            .updateAcToken(
                                    newAccessToken,
                                    userModel
                            );
                    resultMapToken = loginFilterJWTUtil.returnMapMyTokenVerify(newAccessToken);
                }

                // claim 안에 refresh가 있는지 체크해주기

                username  = resultMapToken.get(1).getClaim("username").asString();

                UserModel userModel =  userModelRepository.findByUsername(username);


                PrincipalDetails principalDetails = new PrincipalDetails(userModel);
                // 사용자 인증 , 강제로 객체생성 , 마지막 인자를 보면 꼭 권한을 알려줘야함
                // Authentication 객체를 생성

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                // 세션 공간, 강제로 시큐리티 세션에 접근, Authentication 객체를 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // 여기서 만약 또 리프레시마저 만료라면 재 로그인 시도를 유도해야함
            if(resultMapToken.containsKey(-2)){
                chain.doFilter(request,response);
            }

        }catch (NullPointerException e){
            // 토큰,리프레시 토큰 검색시 결과값이 없을때 , 회원가입을 다시해야하거나
            // 다양한 문제가 발생

            // 즉 verfy 를 실패했을때
            log.info("JWTCheckFilter username null");
        }
        // 정상적으로 서명이 완료되고 반환값이 있을 예정

        chain.doFilter(request,response);
    }
}
