package com.auction.nowauctionb.configpack.jwtconfig;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;



import  com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.IOException;

import java.security.GeneralSecurityException;

import java.security.PublicKey;

import java.time.Instant;
import java.util.Collections;


// 해당 클래스는 의존성 주입이 필요한 클래스
// 컴포넌트로 IOC에 등록해줘야함

@Log4j2
@Component
public class JWTUtil {

    @Value("${googlelogin.googleClientId}")
    private String googleClientId;


    @Value("${googlelogin.googlekey}")
    private String googlekey;

    // @Value 는 정적 변수로는 담지 못함
    // 토큰 검증에 필요한 키
    @Value("${myToken.myKey}")
    private String myKey;


    private static final long AUTH_TIME = 20*60;

    private static final long REFRESH_TIME = 60*60*24*7;

    // null 값 방지를 위해서 @PostConstruct 를 이용해 값을 강제로 주입했습니다
//    @PostConstruct
//    public void init(){
//        ALGORITHM = Algorithm.HMAC256(googleSecreKkey);
//        MYALGORITHM = Algorithm.HMAC256(auctionKey);
//    }

    // 유저네임을 넣어서, 이 유저는 로그인하는 사용자라는 최소한의 정보만을 이용

    //  withExpiresAt => 유효 시간을 정해줌, 우리는 클레임의 exp로 지정
    public String makeAuthToken(UserModel user){
        log.info("now New make Token : " + user.getUsername());
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("nowAuction")
                .withClaim("username", user.getUsername()) // 유저이름
                .withClaim("userRole",user.getRoleList())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(Algorithm.HMAC256(myKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
    }

    // 유저네임을 넣은 Refresh  Token
    public String makeRfreshToken(UserModel user){
        log.info("now New make refresh Token : " + user.getUsername());
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("nowAuction")
                .withClaim("refresh","refresh")
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(Algorithm.HMAC256(myKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
        // 만료시간은 리프레쉬 토큰 시간에 맞춰서 넣는다
    }

    // 단순 디코더, 유효성 검사는 하지 않는다
    public DecodedJWT onlyDecode(String token){

        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public Payload googleVerify(String token) throws GeneralSecurityException, IOException {
        if (googlekey ==null){
            log.info("googlekey null");
        }

        GoogleIdTokenVerifier googleIdTokenVerifier =
                new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(googleClientId))
                        .setIssuer("https://accounts.google.com")
                        .build();

        GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(token);

        // 값이 나오면 인증 성공
        // 설명이 조금 부실하긴함 인증 하지만 암호화 검증이 제대로 이루어졌는지에 대한 궁금증
        if(googleIdToken != null){
            Payload payload = googleIdToken.getPayload();

            log.info("Verification success");
            //log.info(payload.getSubject());
            //log.info(payload.getEmail());
            String name = (String) payload.get("name");
            log.info(name);
            return payload;
        }else{
            log.info("Verification failed");
            return null;
        }


    }


    // 확인, 입증
    public DecodedJWT auctionVerify(String token){

        try{
            // 만약에 인증에 성공한다면
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(myKey)).build().verify(token);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
            // 만약에 인증이 실패했을때 해당 토큰을 디코딩 해봅니다.
            // 인증이 아닌 단순 디코드
            DecodedJWT decode = JWT.decode(token);
            // 서브젝트에 username을 넣어놨기 때문에
            // 해당 토큰의 유저네임을 반환받아봅니다.
            return decode;
        }
    }


}
