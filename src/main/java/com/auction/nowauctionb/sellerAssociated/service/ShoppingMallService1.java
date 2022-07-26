package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;


import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShoppingMallService1 {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    @Transactional
    public int SaveNewShoppingMall(Authentication authentication,
                                   MultipartFile multipartFile,
                                   String shoppingMallName,
                                   String shoppingMallExplanation,
                                   HttpServletRequest request){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 12길이보다 길면
        if(shoppingMallName.length() > 12){
            return -5;
        }


        ShoppinMallModel shoppinMallModel = shoppingMallModelRepositry.findByShoppingMallName(shoppingMallName);

        if(shoppinMallModel != null ){
            // 중복된 쇼핑몰이름
            return -2;
        }

        // 파일저장
        String fileName = makeFile.makeFileImage(principalDetails.getUserModel(), multipartFile,request);

        ShoppinMallModel shoppinMallModelSave =
                ShoppinMallModel.builder()
                        .shoppingMallName(shoppingMallName)
                        .shppingMallExplanation(shoppingMallExplanation)
                        .thumnail(fileName)
                        .userModel(principalDetails.getUserModel())
                        .build();

        shoppingMallModelRepositry.save(shoppinMallModelSave);

        return 1;
    }
}
