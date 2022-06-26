package com.auction.nowauctionb.loginjoin.service;

import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenJoinService {

    @Value("${myToken.dbsecretkey}")
    private String dbsecretkey;

    //처음 가입할때는 무조건 일반회원
    private final UserModelRepository userModelRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTUtil jwtUtil;

    @Transactional
    public int joinGoogleUser(UserModel userModel){

        //userModel.setPassword(userModel.getPassword());

        // 받아온 비밀번호를 다시 앞 id를 붙여줘야함

        // 새로운 시큐리티 버전으로 오면서 이렇게 변경됨
        //userModel.setPassword("{bcrypt}"+userModel.getPassword());

        try {
            userModelRepository.save(userModel);
            return 1;

        }catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }



    public int checkGoogleUser(String email){
        return 2;
    }

    @Transactional(readOnly = true)
    public UserModel findByUsername(String username){

        return userModelRepository.findByUsername(username);
    }

    @Transactional
    public int googleTokenJoingetHeader(HttpServletRequest request){

        if (dbsecretkey == null){
            log.info("tokenService key is null");
        }

        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println();
        System.out.println(headerAuth);

        // 즉 토큰 요청일때
        if(headerAuth != null){
            GoogleIdToken.Payload googlepayload = null;
            try {
                googlepayload = jwtUtil.googleVerify(headerAuth.substring("Bearer ".length()));
                UserModel userModel = userModelRepository.findByUsername(googlepayload.getEmail());

                if(userModel == null){
                    // 여기서 걸렸다는 건 로그인 시 최초가입이라는 뜻
                    log.info("This account does not exist");
                    UserModel joinUserModel = UserModel.builder()
                            .password(bCryptPasswordEncoder.encode(dbsecretkey))
                            .roles("ROLE_USER")
                            .nickname((String) googlepayload.get("name"))
                            .picture((String) googlepayload.get("picture"))
                            .username(googlepayload.getEmail())
                            .oauthname("Google").build();

                    userModelRepository.save(joinUserModel); // save 임
                    log.info("save new user email: "+ googlepayload.get("email") );
                }

            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




        }

        return 1;
    }


}
