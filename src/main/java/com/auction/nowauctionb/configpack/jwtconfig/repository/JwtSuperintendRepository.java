package com.auction.nowauctionb.configpack.jwtconfig.repository;


import com.auction.nowauctionb.configpack.jwtconfig.model.JwtSuperintendModel;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSuperintendRepository extends JpaRepository<JwtSuperintendModel, Integer> {


    JwtSuperintendModel findByUser(UserModel userModel);



}
