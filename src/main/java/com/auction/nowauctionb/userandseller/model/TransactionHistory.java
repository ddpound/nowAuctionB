package com.auction.nowauctionb.userandseller.model;


import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class TransactionHistory {

    // 거래내역 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // 제품 등록자
    @ManyToOne
    @JoinColumn(referencedColumnName = "userId", name = "seller")
    private UserModel seller;

    // 제품 구매자
    @ManyToOne
    @JoinColumn(referencedColumnName = "userId" , name = "buyer")
    private UserModel buyer;

    // 이 테이블 안에는 한개의 제품만을 나타낸다
    @ManyToOne
    @JoinColumn(name = "buyproduct")
    ProductModel buyProduct;

    // 거래가 완료됐는지 안됐는지 파악
    private boolean dealCompleted;

    // 거래완료 날짜
    private Timestamp dealCompleteDate;

    // 거래 등록, 신청날짜
    @CreationTimestamp
    private Timestamp transactionregisterDate;

}
