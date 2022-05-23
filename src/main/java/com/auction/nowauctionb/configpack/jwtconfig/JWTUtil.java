package com.auction.nowauctionb.configpack.jwtconfig;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.Instant;

@Component
public class JWTUtil {

    @Value("${googlelogin.googlekey}")
    private String googlekey;

    // @Value 는 정적 변수로는 담지 못함
    @Value("${myToken.myKey}")
    private String myKey;


    private static final long AUTH_TIME = 20*60; // 테스트를 위해 유효기간을 2초만 둔다

    private static final long REFRESH_TIME = 60*60*24*7;

    // null 값 방지를 위해서 @PostConstruct 를 이용해 값을 강제로 주입했습니다
//    @PostConstruct
//    public void init(){
//        ALGORITHM = Algorithm.HMAC256(googleSecreKkey);
//        MYALGORITHM = Algorithm.HMAC256(auctionKey);
//    }

    // 유저네임을 넣어서, 이 유저는 로그인하는 사용자라는 최소한의 정보만을 이용
    public String makeAuthToken(UserModel user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("nowAuction")
                .withClaim("userRole",user.getRoleList())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(Algorithm.HMAC256(myKey));

        // EpochSecond 에폭세컨드를 이용해 exp이름을 붙여 직접 시간을 지정해준다
    }

    // 유저네임을 넣은 Refresh  Token
    public String makeRfreshToken(UserModel user){
        return JWT.create()
                .withSubject(user.getUsername())
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



    // 구글의 알고리즘은 RS256이다
    // 공개 키 아이디를 가져와야함
    public DecodedJWT verify(String token){



        System.out.println("yml 가져온 변수값 확인 : " + googlekey);
        // 현재 구글 토큰은 비대칭키 알고리즘 RS256을 사용
        //Algorithm algorithm = Algorithm.RSA256(new RsAPu,googlekey);

        // 임시방편 알고리즘
        Algorithm algorithm = Algorithm.HMAC256(googlekey);

        System.out.println("altorithm null 값 확인 :" + algorithm);

        try{
            // 만약에 인증에 성공한다면


            DecodedJWT verify = JWT.require(algorithm).build().verify(token);
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
