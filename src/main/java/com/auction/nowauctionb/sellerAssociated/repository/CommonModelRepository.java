package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.sellerAssociated.model.CommonModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonModelRepository extends JpaRepository<CommonModel,Integer> {

    /**
     * board 게시판 작성자를 찾아서 리스트로 받기
     * */
    List<CommonModel> findAllByShoppingMall(ShoppingMallModel shoppingMallModel);
}
