package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.sellerAssociated.jpamappinginterface.ShoppingMallMapping;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingMallModelRepositry extends JpaRepository<ShoppinMallModel,Integer> {

    ShoppinMallModel findByUserModel(UserModel userModel);

    // 이름 중복 검사
    ShoppinMallModel findByShoppingMallName(String shoppingMallName);

    void deleteByUserModel(UserModel userModel);


    List<ShoppingMallMapping> findAllBy();

}
