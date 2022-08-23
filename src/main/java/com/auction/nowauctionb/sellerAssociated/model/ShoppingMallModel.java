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
public class ShoppingMallModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String shoppingMallName;

    private String shoppingMallExplanation;

    @JoinColumn(name = "create_user")
    @OneToOne
    private UserModel userModel;

    // 사진 경로를 남길듯
    @Column(length = 1000)
    private String thumbnailUrlPath;

    // 삭제를 위한 사진경로
    @Column(length = 1000)
    private String thumbnailFilePath;

    /**
     * 이미지파일이 저장된 고유 폴더 경로
     * */
    @Column(length = 500)
    private String filefolderPath;

    @CreationTimestamp
    private Timestamp createShoppingMall;

    @UpdateTimestamp
    private Timestamp modifyShoppingMall;

}
