package com.auction.nowauctionb.loginjoin.service;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenJoinService {

    //처음 가입할때는 무조건 일반회원
    @Autowired
    private UserModelRepository userModelRepository;

    @Transactional
    public int joinGoogleUser(UserModel userModel){

        // 받아온 비밀번호를 다시 앞 id를 붙여줘야함
        // 새로운 시큐리티 버전으로 오면서 이렇게 변경됨
        userModel.setPassword("{bcrypt}"+userModel.getPassword());

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
    public UserModel findByEmailSer(String email){

        return userModelRepository.findByEmail(email);
    }


}
