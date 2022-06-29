package com.auction.nowauctionb.sellerAssociated.controller;


import com.auction.nowauctionb.sellerAssociated.repository.SellerCouponRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GiveSellerController {



    @PostMapping("give-seller")
    public ResponseEntity giveSeller(){


        return new ResponseEntity(HttpStatus.OK);
    }


}
