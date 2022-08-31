package com.auction.nowauctionb.sellerAssociated.service;


import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.CommonModel;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.CommonModelRepository;
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
public class ShoppingMallServiceAll {

    // 해당서비스는 모든 유저들이 접근가능한 서비스

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final CommonModelRepository commonModelRepository;

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

    @Transactional(readOnly = true)
    public ProductModel findProductModel(int productId){
        Optional<ProductModel> productModel =  productModelRepository.findById(productId);

        productModel.get().getShoppingMall().getUserModel().setPassword("");
        return productModel.get();
    }

    /**
     * 판매자가 작성한 글의 리스트를 가져와주는 함수
     * */
    @Transactional(readOnly = true)
    public List<CommonModel> findAllCommonModel(int shoppingMallId){

        Optional<ShoppingMallModel>shoppingMallModel = shoppingMallModelRepositry.findById(shoppingMallId);

        List<CommonModel> commonModelList =  commonModelRepository.findAllByShoppingMall(shoppingMallModel.get());

        for (CommonModel c: commonModelList
             ) {
            c.getShoppingMall().getUserModel().setPassword("");
        }

        return commonModelList;
    }

    /**
     * 판매자가 작성한 글을 가져와주는 함수
     * */
    @Transactional(readOnly = true)
    public CommonModel findCommonModel(int boardId){
        Optional<CommonModel> commonModel =  commonModelRepository.findById(boardId);

        commonModel.get().getShoppingMall().getUserModel().setPassword("");
        return commonModel.get();
    }




}
