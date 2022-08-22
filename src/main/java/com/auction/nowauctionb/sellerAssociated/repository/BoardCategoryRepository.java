package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.sellerAssociated.model.BoardCategory;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Integer> {



    List<BoardCategory> findAllByShoppingMall(ShoppingMallModel shoppingMallModel);

    BoardCategory findByShoppingMall(ShoppingMallModel shoppingMallModel);

    BoardCategory findByCategoryName(String categoryName);
}
