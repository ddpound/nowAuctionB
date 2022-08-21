package com.auction.nowauctionb.sellerAssociated.model;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.*;

import javax.persistence.*;

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
    private String categoryName;

}
