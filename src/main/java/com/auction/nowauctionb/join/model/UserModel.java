package com.auction.nowauctionb.join.model;


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

    private String username;

    private String password;

    private String roles; // USER, ADMIN, Seller

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            // , 로 스플릿 해서 배열로 리턴해준다
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }


}
