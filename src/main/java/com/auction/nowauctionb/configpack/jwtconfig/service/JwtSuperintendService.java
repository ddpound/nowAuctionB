package com.auction.nowauctionb.configpack.jwtconfig.service;


import com.auction.nowauctionb.configpack.jwtconfig.model.JwtSuperintendModel;
import com.auction.nowauctionb.configpack.jwtconfig.repository.JwtSuperintendRepository;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 토큰 디비 체크하기 , 디비저장된 토큰들 추적 관리 서비스

@Log4j2
@RequiredArgsConstructor
@Service
public class JwtSuperintendService {

    private final JwtSuperintendRepository jwtSuperintendRepository;

    private final UserModelRepository userModelRepository;



    // 레파지토리를 통해 토큰을 저장할대 사용
    // 이미있다면 수정도 해야함
    @Transactional
    public int saveCheckTokenRepository(String getUserName,
                                        String makeMyToken,
                                        String makeRefleshToken){

        // 저장하기 앞서 먼저 검사부터 해야함 DB에 이미 있는지

        // 이미 있는 DB값
        UserModel userModel =  userModelRepository.findByUsername(getUserName);



        JwtSuperintendModel findJwtSuperintendModel = jwtSuperintendRepository.findByUser(userModel);


        if(findJwtSuperintendModel != null ){

            // 이미 있는거니깐 수정, 더티체킹
            findJwtSuperintendModel.setAccessToken(makeMyToken);
            findJwtSuperintendModel.setRefreshToken(makeRefleshToken);
            log.info("changeToken");
            return 2; // 수정을 뜻함

        }else{
            // 처음이라면 새로저장
            JwtSuperintendModel jwtSuperintendModel = JwtSuperintendModel.builder()
                    .user(userModel)
                    .accessToken(makeMyToken)
                    .refreshToken(makeRefleshToken)
                    .build();
            // 더티체킹
            userModel.setJwtSuperintendModel(jwtSuperintendModel);
            jwtSuperintendRepository.save(jwtSuperintendModel);
        }

        return 1;
    }





}
