package com.auction.nowauctionb.chatroom.repository;

import com.auction.nowauctionb.chatroom.chatmodel.ChatRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomModel,Integer> {
}
