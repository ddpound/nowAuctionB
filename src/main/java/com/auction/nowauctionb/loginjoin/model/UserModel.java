package com.auction.nowauctionb.loginjoin.model;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// UserModel Entiry


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    // 구글등의 닉네임이 담길 예정
    private String nickname;

    // 이메일이 담길예정, 중복 불가
    // username이 이메일
    private String username;

    // 토큰값을 넣을까 고민중
    private String password;

    // 어디 로그인api를 사용했는지도 구분해놔야함
    private String oauthname;

    private String roles; // USER, ADMIN, Seller

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            // , 로 스플릿 해서 배열로 리턴해준다
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    //최초 로그인 날짜와


    //가장 마지막으로 로그인한 날짜를 정해주자

    // 그리고 만료


}
