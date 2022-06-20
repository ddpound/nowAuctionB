package com.auction.nowauctionb.userAssociated.controller;


import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.userAssociated.frontmodel.UserModelFront;
import com.auction.nowauctionb.userAssociated.service.UserService1;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping(value = "user/delete/{username}")
    public String deleteUser(@PathVariable String username,
                             Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        // localstorage에 저장된 녀석과 토큰에 저장된 애가 제대로오는지확이
        if(principalDetails.getUsername().equals(username)){
            userService1.deleteUser(username);
            return "success";
        }


        return null;
    }

    @GetMapping(value = "/user/test1")
    public String testAuthoriContriller(){
        System.out.println("유저권한 컨트롤러 체크");
        return "유저 권한";
    }


}
