package com.auction.nowauctionb.configpack;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;



public class JWTUtil {


    @Value("${googlelogin.secretkey}")
    private static String secretkey;

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(secretkey);

    private static final long AUTH_TIME = 2; // 테스트를 위해 유효기간을 2초만 둔다

    private static final long REFRESH_TIME = 60*60*24*7;

    // 원래는 구분용도로 claim을 넣어줘도 되지만 일단 테스트용이니깐 넘어갑니다!


    // 확인, 입증
    public static DecodedJWT verify(String token){
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
}
