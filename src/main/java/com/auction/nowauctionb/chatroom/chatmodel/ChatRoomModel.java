package com.auction.nowauctionb.chatroom.chatmodel;


import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.*;

import javax.persistence.*;

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

    private String title;

    @JoinColumn(name = "host")
    @OneToOne
    private UserModel userModel;


    private String thumbnail;



}
