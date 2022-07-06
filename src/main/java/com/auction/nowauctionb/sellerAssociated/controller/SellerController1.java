package com.auction.nowauctionb.sellerAssociated.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class SellerController1 {

    // 방만들기
    // 내 쇼핑몰,공간 만들기
    // 게시판 글 작성하기


    // 채팅방 개설
    @PostMapping("make-room")
    public ResponseEntity makeRoom(){

        return null;
    }


    // 쇼핑몰 만들기
    @PostMapping("make-shopping-mall")
    public ResponseEntity makeMyShoppingMall(){

        return null;
    }


}
