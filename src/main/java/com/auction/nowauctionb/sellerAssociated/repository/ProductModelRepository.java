package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductModelRepository extends JpaRepository<ProductModel,Integer> {


    List<ProductModel> findAllByShoppingMall(Optional<ShoppingMallModel> shoppingMallModel);
    List<ProductModel> findAllByShoppingMall(ShoppingMallModel shoppingMallModel);
}
