package com.auction.nowauctionb.userAssociated.frontmodel;


// 프론트쪽에 전달할 기본 정보들 모델 오브젝트
// 공개적인 자료들만 구성됨

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModelFront {

    private int id;

    // 이메일
    private String userName;

    // 룰을 저장 (보통 ROLE_* 이기떄문에 ROLE_을 제거해주고 보내주자)
    // user -> 일반, seller -> 판매자, admin -> 관리자

    private String role;

    // 닉네임
    private String nickName;

    private String picture;





}
