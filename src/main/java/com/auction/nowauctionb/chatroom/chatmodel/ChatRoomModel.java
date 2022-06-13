package com.auction.nowauctionb.chatroom.chatmodel;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// 채팅방 관련 DB

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoomModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;





}
