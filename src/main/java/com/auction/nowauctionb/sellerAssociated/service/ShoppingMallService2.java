package com.auction.nowauctionb.sellerAssociated.service;


import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.ProductModelRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallService2 {

    // 해당서비스는 모든 유저들이 접근가능한 서비스

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final ProductModelRepository productModelRepository;

    @Transactional(readOnly = true)
    public Optional<ShoppingMallModel> findShoppingMall(int shoppingMall){

        Optional<ShoppingMallModel> findShoppingMall = shoppingMallModelRepositry.findById(shoppingMall);
        findShoppingMall.get().getUserModel().setPassword("");
        return findShoppingMall;
    }

    @Transactional(readOnly = true)
    public List<ProductModel> findAllProduct(int shoppingmallId){

        ArrayList<ProductModel> listProductModel = new ArrayList<>();

        // 패스워드는 널값으로
        for (ProductModel p : productModelRepository.findAllByShoppingMall(shoppingMallModelRepositry.findById(shoppingmallId))
             ) {
            p.getShoppingMall().getUserModel().setPassword("");
            listProductModel.add(p);
        }

        return listProductModel;
    }




}
