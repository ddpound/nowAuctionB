package com.auction.nowauctionb.sellerAssociated.model;

import com.auction.nowauctionb.admin.model.AdminBoardCategory;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // 퀄리티와 다른 퀀티티, 즉 수량을 말함
    private int quantity;

    // 제품 설명, surmmernote로 할 예정
    @Lob
    private String Content;

    // 사진 경로 , 및 썸네일 대표 3장씩 나오도록 하자
    @Lob
    private String Pictures;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;

    public List<String> getPicturesList(){
        if(this.Pictures.length() > 0){
            // , 로 스플릿 해서 배열로 리턴해준다
            return Arrays.asList(this.Pictures.split(","));
        }
        return new ArrayList<>();
    }

}
