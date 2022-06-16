package com.auction.nowauctionb.userAssociated.controller;


import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.userAssociated.frontmodel.UserModelFront;
import com.auction.nowauctionb.userAssociated.service.UserService1;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserController1 {

    private final UserService1 userService1;

    @GetMapping(value = "user/info")
    public UserModelFront getUserName(Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        log.info("return / info : " + principalDetails);

        return userService1.findUserNameFrontUserModel(principalDetails.getUsername());
    }


    @GetMapping(value = "/user/test1")
    public String testAuthoriContriller(){
        return "유저 권한";
    }


}
