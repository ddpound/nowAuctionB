package com.auction.nowauctionb.admin.controller;


import com.auction.nowauctionb.admin.service.AdminService1;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class giveAdminController {

    private final AdminService1 adminService1;

    @GetMapping(value = "give-admin/{adminpassword}")
    public ResponseEntity giveMeAdmin(@PathVariable("adminpassword")String password,
                                      Authentication authentication){
        System.out.println(password);
        adminService1.giveAdmin(authentication,password);

        return new ResponseEntity(HttpStatus.OK);
    }



}
