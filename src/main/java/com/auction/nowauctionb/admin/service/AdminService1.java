package com.auction.nowauctionb.admin.service;


import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService1 {

    private final UserModelRepository userModelRepository;

    @Value("${jangadmin.secreyKey}")
    private String adminPassword;



    //체크 필터에서 가져온 authentication 값을 디비에 넣고 비밀번호도 받아와야함
    @Transactional
    public int giveAdmin(Authentication authentication,String password){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        //저장해둔 비밀번호가 같다면
        if(adminPassword.equals(password)){
            //  db에서 세팅을 바꿔주자


            System.out.println(principalDetails.getUsername() + "   ㅁㅁㅁㅁㅁㅁ");

            // 영속성 , 더티체킹
            UserModel userModel = userModelRepository.findByUsername(principalDetails.getUsername());
            userModel.setRoles("ROLE_USER,ROLE_SELLER,ROLE_ADMIN");

        }

        return 1;
    }


}
