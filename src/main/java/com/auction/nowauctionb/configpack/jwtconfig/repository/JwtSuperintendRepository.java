package com.auction.nowauctionb.configpack.jwtconfig.repository;


import com.auction.nowauctionb.configpack.jwtconfig.model.JwtSuperintendModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSuperintendRepository extends JpaRepository<JwtSuperintendModel, Integer> {
}
