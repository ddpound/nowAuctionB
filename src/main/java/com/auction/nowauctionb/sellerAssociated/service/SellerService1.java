package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.SellerCouponRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class SellerService1 {

    private final SellerCouponRepository sellerCouponRepository;

    //권한 등록을 바꿔줘야함
    private final UserModelRepository userModelRepository;

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    @Transactional
    public int sellerRegister(Authentication authentication, String id, String code){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        UserModel userModel = userModelRepository.findByUsername(principalDetails.getUsername());
        SellerCoupon findSellerCoupon =
                sellerCouponRepository.findByIdAndCouponPassword(Integer.parseInt(id),code);

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
    public ShoppinMallModel checkShoppingMall(PrincipalDetails principalDetails){

        return shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

    }









}
