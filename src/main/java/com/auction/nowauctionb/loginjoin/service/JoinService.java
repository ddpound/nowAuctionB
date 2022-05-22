package com.auction.nowauctionb.loginjoin.service;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    //처음 가입할때는 무조건 일반회원

    @Autowired
    UserModelRepository userModelRepository;

    public int JoinGoogleUser(UserModel userModel){

        userModelRepository.save(userModel);
        return 1;
    }




}
