package com.auction.nowauctionb.sellerAssociated.model;


import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class ShoppinMallModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String ShoppingMallName;

    @JoinColumn(name = "create_user")
    @OneToOne
    private UserModel userModel;

    // 사진 경로를 남길듯
    @Column(length = 800)
    private String thumnail;

    @CreationTimestamp
    private Timestamp createShoppinMall;

    @UpdateTimestamp
    private Timestamp modifyShoppinMall;

}
