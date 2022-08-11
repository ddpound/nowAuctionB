package com.auction.nowauctionb.sellerAssociated.controller;


import com.auction.nowauctionb.admin.model.IntegrateBoardModel;
import com.auction.nowauctionb.chatroom.chatmodel.ChatRoomModel;
import com.auction.nowauctionb.chatroom.service.ChatSellerService1;
import com.auction.nowauctionb.sellerAssociated.jpamappinginterface.ShoppingMallMapping;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.service.ShoppingMallService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AllAccessibleSellerController {

    private final ShoppingMallService1 shoppingMallService1;

    private final ChatSellerService1 chatSellerService1;

    // 모든 쇼핑몰 찾기
    @GetMapping(value = "find-all-shopping-mall")
    public ResponseEntity<List<ShoppinMallModel>> findAllShoppingMallList(){
        return new ResponseEntity<>(shoppingMallService1.findAllShoppingMallList(), HttpStatus.OK);
    }

    // 모든 채팅 룸 찾기
    @GetMapping(value = "find-all-chat-room")
    public ResponseEntity<List<ChatRoomModel>> findAllChatRoomList(){

        return new ResponseEntity<>(chatSellerService1.findAllChatRoom(),HttpStatus.OK);
    }

}
