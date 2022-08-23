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
public class CommonModel {

    // 해당 모델은 게시글 작성을 위한 공통 모델입니다.
    // 썸네일은 가장 첫번째 사진이 담당할 예정
    // 기본 카테고리는 공지가 주어질 예정
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    @ManyToOne
    @JoinColumn
    private UserModel userModel;

    @Lob
    private String Content;

    // 사진 경로 , 및 썸네일 대표 3장씩 나오도록 하자
    // 3장의 경로를 담을예정이니 길이가 길어야함
    @Column(length = 1000)
    private String pictureFilePath;

    @Column(length = 1000)
    private String pictureUrlPath;

    // 이미지파일이 저장된 고유 폴더 경로
    @Column(length = 500)
    private String filefolderPath;

    @ManyToOne
    @JoinColumn(name = "board_category")
    private BoardCategory boardCategory;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;
}
