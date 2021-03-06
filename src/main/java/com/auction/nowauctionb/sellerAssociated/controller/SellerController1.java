package com.auction.nowauctionb.sellerAssociated.controller;

import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.service.SellerService1;
import com.auction.nowauctionb.sellerAssociated.service.ShoppingMallService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class SellerController1 {

    // 방만들기
    // 내 쇼핑몰,공간 만들기
    // 게시판 글 작성하기

    private final SellerService1 sellerService1;

    private final ShoppingMallService1 shoppingMallService1;

    // 채팅방 개설
    @PostMapping("make-room")
    public ResponseEntity makeRoom(){

        return null;
    }

    @GetMapping("check-mall")
    public ResponseEntity shoppingMallCheck(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        return new ResponseEntity(sellerService1.checkShoppingMall(principalDetails),HttpStatus.OK);
    }

    // 쇼핑몰 만들기
    @PostMapping("make-shopping-mall")
    public ResponseEntity makeMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                             @RequestParam("thumbnail") MultipartFile multipartFile,
                                             @RequestParam("explantion") String shoppingMallExplanation,
                                             Authentication authentication) throws IOException {

        shoppingMallService1.SaveNewShoppingMall(authentication , multipartFile,shoppingmallName , shoppingMallExplanation);


        return new ResponseEntity("", HttpStatus.OK);
    }


}
