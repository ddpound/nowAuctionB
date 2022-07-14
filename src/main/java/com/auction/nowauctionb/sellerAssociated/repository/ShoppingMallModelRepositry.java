package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingMallModelRepositry extends JpaRepository<ShoppinMallModel,Integer> {

    ShoppinMallModel findByUserModel(UserModel userModel);

}
