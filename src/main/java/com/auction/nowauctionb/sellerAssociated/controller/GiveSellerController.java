package com.auction.nowauctionb.sellerAssociated.controller;



import com.auction.nowauctionb.sellerAssociated.service.SellerService1;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class GiveSellerController {

    private final SellerService1 sellerService1;

    @PostMapping("give-seller")
    public ResponseEntity giveSeller(Authentication authentication, @RequestBody Map<String,Object> coupon){

        int resultNum = sellerService1
                .sellerRegister(
                        authentication,
                        coupon.get("id").toString(),
                        coupon.get("code").toString());
        if(resultNum ==1 ){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity("It's a coupon I've already used", HttpStatus.FORBIDDEN);
        }


    }


}
