package com.auction.nowauctionb.configpack;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
public class JWTUtil {

    // @Value 는 정적 변수로는 담지 못함
    @Value("${googlelogin.secretkey}")
    private String secretkey;

    private Algorithm ALGORITHM;

    private static final long AUTH_TIME = 2; // 테스트를 위해 유효기간을 2초만 둔다

    private static final long REFRESH_TIME = 60*60*24*7;

    // null 값 방지를 위해서 @PostConstruct 를 이용해 값을 강제로 주입했습니다
    @PostConstruct
    public void init(){
        ALGORITHM = Algorithm.HMAC256(secretkey);
    }



    public DecodedJWT verify(String token){

        try{
            // 만약에 인증에 성공한다면
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return verify;
        }catch (Exception e){
            // 만약에 인증이 실패했을때 해당 토큰을 디코딩 해봅니다.
            // 인증이 아닌 단순 디코드
            DecodedJWT decode = JWT.decode(token);
            // 서브젝트에 username을 넣어놨기 때문에
            // 해당 토큰의 유저네임을 반환받아봅니다.
            return decode;
        }
    }


    // 확인, 입증


}
