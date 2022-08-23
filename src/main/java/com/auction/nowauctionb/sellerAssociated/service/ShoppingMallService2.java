package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.repository.BoardCategoryRepository;
import com.auction.nowauctionb.sellerAssociated.repository.CommonModelRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ProductModelRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallService2 {


    // 해당 서비스는 수정 관련된 애들만 다 모아둠
    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final ProductModelRepository productModelRepository;

    private final CommonModelRepository commonModelRepository;

    private final BoardCategoryRepository boardCategoryRepository;




}
