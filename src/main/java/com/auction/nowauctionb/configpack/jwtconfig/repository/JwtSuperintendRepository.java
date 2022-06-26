package com.auction.nowauctionb.configpack.jwtconfig.repository;


import com.auction.nowauctionb.configpack.jwtconfig.model.JwtSuperintendModel;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JwtSuperintendRepository extends JpaRepository<JwtSuperintendModel, Integer> {


    JwtSuperintendModel findByUser(UserModel userModel);

    JwtSuperintendModel findByAccessTokenAndRefreshToken(String AccessToken, String RefreshToken);

    void deleteByUser(UserModel userModel);

    @Transactional
    @Modifying
    @Query("update JwtSuperintendModel set accessToken = :accessToken, " +
            "refreshToken = :refreshToken , modifyToken = current_timestamp where user = :user")
    void updateAcTokenRefreshToken(@Param("accessToken")String accessToken,
                                   @Param("refreshToken")String refreshToken,
                                   @Param("user") UserModel user);

    @Transactional
    @Modifying
    @Query("update JwtSuperintendModel set accessToken = :accessToken," +
            "modifyToken = current_timestamp where user = :user")
    void updateAcToken(@Param("accessToken")String accessToken,
                       @Param("user") UserModel user);
}
