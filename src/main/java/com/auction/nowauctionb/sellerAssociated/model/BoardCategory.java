package com.auction.nowauctionb.sellerAssociated.model;

import com.auction.nowauctionb.loginjoin.model.UserModel;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // 어디 쇼핑몰의 카테고리인지 확인
    @ManyToOne
    @JoinColumn(name = "shopping_mall")
    private ShoppingMallModel shoppingMall;

    @Lob
    @Column(nullable = false,unique = true)
    private String categoryName;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;

}
