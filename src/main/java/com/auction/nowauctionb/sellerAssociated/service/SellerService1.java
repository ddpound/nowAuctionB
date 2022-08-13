package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.admin.model.IntegrateBoardModel;
import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auction.nowauctionb.sellerAssociated.frontmodel.ShoppingMallFront;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.ProductModelRepository;
import com.auction.nowauctionb.sellerAssociated.repository.SellerCouponRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Service
public class SellerService1 {

    private final SellerCouponRepository sellerCouponRepository;

    //권한 등록을 바꿔줘야함
    private final UserModelRepository userModelRepository;

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;



    private final MakeFile makeFile;

    @Transactional
    public int sellerRegister(Authentication authentication, String id, String code){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        UserModel userModel = userModelRepository.findByUsername(principalDetails.getUsername());
        SellerCoupon findSellerCoupon = null;
        try {
            findSellerCoupon = sellerCouponRepository.findByIdAndCouponPassword(Integer.parseInt(id),code);
        }catch (NumberFormatException e){
            return -5; // id가 String이 아닐때
        }




        // 키값이 날라오지 않음
        if(id.equals("") && code.equals("")){
            return -3;
        }

        // 등록된쿠폰 아님
        if(findSellerCoupon ==null){
            return -1;
        }

        // 만약 이미 등록된 쿠폰 이라는 뜻
        if(findSellerCoupon.getUserModel() != null){
            return -2;
        }

        // 새쿠폰이라면 그럼 쿠폰 등록자를 해주자
        findSellerCoupon.setUserModel(userModel);
        userModel.setRoles("ROLE_USER,ROLE_SELLER");
        return 1;
    }

    @Transactional(readOnly = true)
    public ShoppingMallFront checkShoppingMall(PrincipalDetails principalDetails){
        ShoppinMallModel shoppinMallModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        if(shoppinMallModel !=null){
            UserModel userModel = shoppinMallModel.getUserModel();
            userModel.setPassword("");

            // 프론트 반환할때 비밀번호가 나가지않게... 근데 다른 좋은방법을 생각해보자
            ShoppingMallFront shoppingMallFront = ShoppingMallFront.builder()
                    .id(shoppinMallModel.getId())
                    .shoppingMallName(shoppinMallModel.getShoppingMallName())
                    .shppingMallExplanation(shoppinMallModel.getShoppingMallExplanation())
                    .thumnail(shoppinMallModel.getThumbnailUrlPath())
                    .userModel(userModel)
                    .build();


            return shoppingMallFront;
        }else {
            return null;
        }

    }









}
