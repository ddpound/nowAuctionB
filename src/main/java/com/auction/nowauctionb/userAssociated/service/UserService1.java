package com.auction.nowauctionb.userAssociated.service;

import com.auction.nowauctionb.configpack.jwtconfig.model.JwtSuperintendModel;
import com.auction.nowauctionb.configpack.jwtconfig.repository.JwtSuperintendRepository;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auction.nowauctionb.userAssociated.frontmodel.UserModelFront;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService1 {

    private final UserModelRepository userModelRepository;

    private final JwtSuperintendRepository jwtSuperintendRepository;



    @Transactional(readOnly = true)
    public UserModelFront findUserNameFrontUserModel(String userName){

        // 이메일을 통해서 찾아냄
        UserModel finduserModel = userModelRepository.findByUsername(userName);

        if(finduserModel != null){

            List<String> roleList =  finduserModel.getRoleList();
            // 만약 일반유저가 아니라 여러 유저일경우

            // 그냥 기본적일때 가장 첫번째 것만 가져옴
            String resultRole = roleList.get(0);

            // 1개이상의 권한이 있다면 일반유저 이상
            if(roleList.size() == 2){
                resultRole = roleList.get(1);
            }else if(roleList.size() == 3){
                resultRole = roleList.get(2);
            }

            return UserModelFront.builder()
                    .userName(finduserModel.getUsername())
                    .role(resultRole.replace("ROLE_",""))
                    .nickName(finduserModel.getNickname())
                    .picture(finduserModel.getPicture())
                    .build();

        }

        return null;
    }

    @Transactional
    public int deleteUser(String username){

        // 이메일을 통해서 찾아냄
        UserModel finduserModel = userModelRepository.findByUsername(username);


        try {
            jwtSuperintendRepository.deleteByUser(finduserModel);
            userModelRepository.delete(finduserModel);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

}
