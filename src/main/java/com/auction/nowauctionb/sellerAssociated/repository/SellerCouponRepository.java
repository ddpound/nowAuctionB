package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerCouponRepository extends JpaRepository<SellerCoupon,Integer> {

    SellerCoupon findByUserModel(UserModel userModel);

    SellerCoupon findByCouponPassword(String CouponPassword);

}
