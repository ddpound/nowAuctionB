package com.auction.nowauctionb.configpack.jwtconfig;



import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 주의 해당 클래스는 필터에 직접 주입해야 해서
// 컴포넌트 등록을 권장하진 않는다
// 오직 필터만을 위한 구글 로그인 클래스
// 컴포넌트 등록 하면안됨

@Log4j2
@Getter
public class LoginFilterJWTUtil {


    @Value("${googlelogin.googleClientId}")
    private String googleClientId;


    @Value("${googlelogin.googlekey}")
    private String googlekey;

    @Value("${myToken.dbsecretkey}")
    private String dbsecretkey;

    // @Value 는 정적 변수로는 담지 못함
    @Value("${myToken.myKey}")
    private String myKey;

    private final long AUTH_TIME = 20;

    private final long REFRESH_TIME = 60*60*24*7;

    public GoogleIdToken.Payload googleVerify(String token) throws GeneralSecurityException, IOException {
//        if (googlekey !=null){
//            log.info("dbsecretKey null" + dbsecretkey);
//            log.info("googlekey null" + googlekey);
//        }

        GoogleIdTokenVerifier googleIdTokenVerifier =
                new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(googleClientId))
                        .setIssuer("https://accounts.google.com")
                        .build();

        GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(token);


        // 값이 나오면 인증 성공
        // 설명이 조금 부실하긴함 인증 하지만 암호화 검증이 제대로 이루어졌는지에 대한 궁금증
        if(googleIdToken != null){
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            log.info("GoogleToken Verification success");
            //log.info(payload.getSubject());
            //log.info(payload.getEmail());
            //String name = (String) payload.get("name");
            //log.info(name);
            return payload;
        }else{
            log.info("GoogleToken Verification failed");
            return null;
        }
    }

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



    // 키와 밸류 형식으로 한번에 해주기
    // 1이면 검증완료, -2 이면 만료된 토큰, -1 이면 검증실패, 그냥 토큰이 검증실패
    public Map<Integer,DecodedJWT> returnMapMyTokenVerify(String token){
        Map<Integer,DecodedJWT> returnMap = new HashMap<>();

        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(myKey)).build().verify(token);
            log.info("success myToken verify");
            returnMap.put(1, verify);
            return returnMap;
        }catch (TokenExpiredException e){
            log.info("The myToken has expired"); // 토큰 유효시간이 지남

            DecodedJWT decodeJWT = JWT.decode(token);
            returnMap.put(-2, decodeJWT);

            // 재발급이 필요, 리프레시 토큰이 있나 체크해야함
            return returnMap;
        }

        catch (Exception e){
            //e.printStackTrace();
            DecodedJWT decodeJWT = JWT.decode(token);

            log.info("myToken fail verify : " + decodeJWT);
            // 실패시
            returnMap.put(-1, decodeJWT);
            return returnMap;

        }
    }


    // 단순 디코더해서 안에 값이 뭐가있는지 체크용
    public DecodedJWT simpleDecode(String token){
        DecodedJWT decodeJWT = JWT.decode(token);

        log.info("myToken fail verify : " + decodeJWT);
        return decodeJWT;
    }


}
