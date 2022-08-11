package com.auction.nowauctionb.chatroom.controller;

import com.auction.nowauctionb.chatroom.service.ChatSellerService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class ChatSellerController {

    private final ChatSellerService1 chatSellerService1;

    @PostMapping(value = "register-chatroom")
    public ResponseEntity makeChatRoom(@RequestParam("chatRoomTitle")String chatRoomTitle,
                                       Authentication authentication,
                                       HttpServletRequest request){

        // 임시등록 수정할 예정
        chatSellerService1.chatRoomRegister(chatRoomTitle,authentication,request);

        return new ResponseEntity(null,HttpStatus.OK);
    }


}
