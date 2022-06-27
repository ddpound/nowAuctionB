package com.auction.nowauctionb.sellerAssociated.model;


import com.auction.nowauctionb.loginjoin.model.UserModel;

import javax.persistence.*;

@Entity
public class SellerCoupon {


    // 쿠폰 고유번호
    @Id
    private int id;

    // 쿠폰의 비밀번호
    // 넉넉하게
    @Column(length = 500)
    private String couponPassword;

    // 하나의 유저가 쿠폰을 가진 형태
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;

}
