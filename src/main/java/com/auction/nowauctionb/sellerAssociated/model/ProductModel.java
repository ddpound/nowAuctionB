package com.auction.nowauctionb.sellerAssociated.model;

import com.auction.nowauctionb.admin.model.AdminBoardCategory;
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
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // 제품 등록자
    @ManyToOne
    @JoinColumn
    private UserModel userModel;

    // 제품이름
    private String productName;

    // 제품 가격
    private int productPrice;

    // 제품 설명
    @Lob
    private String Content;

    // 사진 경로
    @Lob
    private String Pictures;


    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;

}
