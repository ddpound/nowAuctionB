package com.auction.nowauctionb.loginjoin.repository;

import com.auction.nowauctionb.loginjoin.model.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel , Integer> {


    // 사실 유저네임으로 받아오지만 이메일로 구분해야함
    UserModel findByUsername(String username);
}
