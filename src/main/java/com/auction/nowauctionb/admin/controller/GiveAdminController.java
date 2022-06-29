package com.auction.nowauctionb.admin.controller;


import com.auction.nowauctionb.admin.service.AdminService1;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class GiveAdminController {

    private final AdminService1 adminService1;

    @PostMapping(value = "give-admin")
    public ResponseEntity giveMeAdmin(@RequestBody Map<String,Object> passwordMap,
                                      Authentication authentication){
        int resultNum = adminService1.giveAdmin(authentication,passwordMap);

        if(resultNum ==1){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }


    }



}
