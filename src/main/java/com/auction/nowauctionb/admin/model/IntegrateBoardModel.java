package com.auction.nowauctionb.admin.model;


import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

// 통합 보드 모델, 한 모델을 통해서 모든 보드들을 재사용 예정
// 게시판, 글쓰기 등등

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class IntegrateBoardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn
    private UserModel userModel;

    private String title;

    @Lob
    private String Content;

    // 카테고리
    // {공지, 그외 관리자가 사용할 보드 통합}
    private AdminBoardCategory adminBoardCategory;

    // 이미지파일이 저장된 고유 폴더 경로
    @Column(length = 500)
    private String filefolderPath;


    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp modifyDate;

}
