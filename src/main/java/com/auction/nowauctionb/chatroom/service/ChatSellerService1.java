package com.auction.nowauctionb.chatroom.service;

import com.auction.nowauctionb.chatroom.chatmodel.ChatRoomModel;
import com.auction.nowauctionb.chatroom.repository.ChatRoomRepository;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChatSellerService1 {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public int chatRoomRegister(String chatRoomTitle,
                                Authentication authentication,
                                HttpServletRequest httpServletRequest){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        ChatRoomModel chatRoomModel = ChatRoomModel.builder()
                .title(chatRoomTitle)
                .userModel(principalDetails.getUserModel())
                .build();

        chatRoomRepository.save(chatRoomModel);


        return 1;
    }


    @Transactional(readOnly = true)
    public List<ChatRoomModel> findAllChatRoom(){

        List<ChatRoomModel> chatRoomModels = chatRoomRepository.findAll();


        // 얘 항상 true라는데 그러니깐 null값이 아니라는데 확인해봐야겠음
        for (int i=0; i<chatRoomModels.size();i++){
            chatRoomModels.get(i).getUserModel().setPassword("");
        }


        // 근데 또 비밀번호를 빼줘야함 이게 아주 골치아픈 상황임
        return chatRoomModels;
    }
}
