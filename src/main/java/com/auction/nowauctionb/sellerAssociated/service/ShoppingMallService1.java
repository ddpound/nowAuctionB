package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.ProductModelRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class ShoppingMallService1 {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final ProductModelRepository productModelRepository;

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
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        Map<Integer,String> fileNames =makeFile.makeFileImage(principalDetails.getUserModel(), multipartFile,request);


        ShoppinMallModel shoppinMallModelSave =
                ShoppinMallModel.builder()
                        .shoppingMallName(shoppingMallName)
                        .shppingMallExplanation(shoppingMallExplanation)
                        .thumbnailUrlPath(fileNames.get(1))
                        .thumbnailFilePath(fileNames.get(2))
                        .userModel(principalDetails.getUserModel())
                        .build();

        shoppingMallModelRepositry.save(shoppinMallModelSave);

        return 1;
    }

    // 수정할때 해야할것
    // 사진이 추가됐다면 해당 사진은 삭제하고
    // 새사진을 올려야함
    @Transactional
    public int modifyShoppingMall(Authentication authentication,
                                   MultipartFile multipartFile,
                                   String shoppingMallName,
                                   String shoppingMallExplanation,
                                   String urlFilePath,
                                   HttpServletRequest request){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 12길이보다 길면
        if(shoppingMallName.length() > 12){
            return -5;
        }


        // 해당유저가 이미 쇼핑몰이 있다는 가정 하에
        // 영속화
        ShoppinMallModel shoppinMallModel1ByUserModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        // 사진이 달라졌을경우 삭제 후 추가
        // 그대로라면 그냥 그대로
        if(shoppinMallModel1ByUserModel == null ) {
            // 해당 유저가 가진 쇼핑몰이 없을 때
            return -3;
        }

        // 비어있다면 아마 새로운 파일을 보낸것일테니 삭제
        if(urlFilePath == null){
            // 새로운 파일을 저장하기 앞서 먼저 삭제해야함 해당 사진은
            // 파일 경로를 불러와 그대로 삭제
            makeFile.filePathImageDelete(shoppinMallModel1ByUserModel.getThumbnailFilePath());
            // 새로운 파일저장 입니다.
            // 1. url 사진 경로
            // 2. 컴퓨터 사진 파일 경로
            Map<Integer,String> fileNames =makeFile.makeFileImage(principalDetails.getUserModel(), multipartFile,request);
            shoppinMallModel1ByUserModel.setThumbnailUrlPath(fileNames.get(1));
            shoppinMallModel1ByUserModel.setThumbnailFilePath(fileNames.get(2));
        }

        // 아니면 냅둔다



        // 더티체킹
        shoppinMallModel1ByUserModel.setShoppingMallName(shoppingMallName);
        shoppinMallModel1ByUserModel.setShppingMallExplanation(shoppingMallExplanation);



        return 1;
    }
    @Transactional
    public int saveProduct(Authentication authentication,
                           String productName,
                           String productPrice,
                           HttpServletRequest request){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        ProductModel productModel = ProductModel.builder()
                .productName(productName)
                .productPrice(Integer.parseInt(productPrice))
                .userModel(principalDetails.getUserModel())
                .build();

        productModelRepository.save(productModel);
        return 1;
    }

    @Transactional(readOnly = true)
    public List<ShoppinMallModel> findAllShoppingMallList(){

        List<ShoppinMallModel> findShoppingMallModel = shoppingMallModelRepositry.findAll();


        if(findShoppingMallModel.size() > 0){
            for (int i=0 ; i < findShoppingMallModel.size(); i++) {
                findShoppingMallModel.get(i).getUserModel().setPassword("");
            }
        }

        return findShoppingMallModel;
    }
}
