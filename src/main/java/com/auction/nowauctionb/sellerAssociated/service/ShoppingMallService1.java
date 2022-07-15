package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;


import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShoppingMallService1 {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;


    public int SaveNewShoppingMall(Authentication authentication,
                                   MultipartFile multipartFile,
                                   String shoppingMallName){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        String fileRoot = "C:"+ File.separator+"Jang_SaveImage"+ File.separator+"thumbnail"+ File.separator;


        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 명 가져오기

        String savedFileName = "Thumbnail-"+ UUID.randomUUID()+ extension;

        File targetFile = new File(fileRoot+savedFileName);


        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            // 설명 읽어보니 닫아놔야한다
            multipartFile.getInputStream().close();
        }catch (IOException e){
            FileUtils.deleteQuietly(targetFile);
        }

        return 1;
    }
}
