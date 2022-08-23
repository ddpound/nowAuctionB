package com.auction.nowauctionb.sellerAssociated.controller;


import com.auction.nowauctionb.chatroom.chatmodel.ChatRoomModel;
import com.auction.nowauctionb.chatroom.service.ChatSellerService1;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import com.auction.nowauctionb.sellerAssociated.service.ShoppingMallService1;
import com.auction.nowauctionb.sellerAssociated.service.ShoppingMallServiceAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *
 * 모든 클라이언트들이 접근가능함
 *
 * */
@RequiredArgsConstructor
@RestController
public class AllAccessibleSellerController {

    private final ShoppingMallService1 shoppingMallService1;

    private final ShoppingMallServiceAll shoppingMallServiceAll;

    private final ChatSellerService1 chatSellerService1;



    // 모든 쇼핑몰 찾기
    @GetMapping(value = "find-all-shopping-mall")
    public ResponseEntity<List<ShoppingMallModel>> findAllShoppingMallList(){
        return new ResponseEntity<>(shoppingMallService1.findAllShoppingMallList(), HttpStatus.OK);
    }

    // 모든 채팅 룸 찾기
    @GetMapping(value = "find-all-chat-room")
    public ResponseEntity<List<ChatRoomModel>> findAllChatRoomList(){

        return new ResponseEntity<>(chatSellerService1.findAllChatRoom(),HttpStatus.OK);
    }

    // 해당 쇼핑 몰 보기
    // 여러개를 요청해야함
    // 1. 해당 쇼핑몰 정보, 2. 해당쇼핑몰이 가진 제품들 전부다 나열
    // 해당 쇼핑몰을 알면
    @GetMapping(value = "show-shoppingmall")
    public ResponseEntity showShoppingMall(@RequestParam("id")int shoppingMallId){

        return new ResponseEntity(shoppingMallServiceAll.findShoppingMall(shoppingMallId), HttpStatus.OK);
    }

    @GetMapping(value = "show-shoppingmall/find-product-all/{id}")
    public ResponseEntity findProductAll(@PathVariable("id")int shoppingMallId){

        return new ResponseEntity(shoppingMallServiceAll.findAllProduct(shoppingMallId),HttpStatus.OK);
    }

    @GetMapping(value = "show-shoppingmall/product-show/{id}")
    public ResponseEntity showProduct(@PathVariable("id")int productlId){

        return new ResponseEntity(shoppingMallServiceAll.findProductModel(productlId),HttpStatus.OK);
    }

}
