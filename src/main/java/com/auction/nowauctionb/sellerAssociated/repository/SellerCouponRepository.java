package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerCouponRepository extends JpaRepository<SellerCoupon,Integer> {

    SellerCoupon findByUserModel(UserModel userModel);

    SellerCoupon findByCouponPassword(String CouponPassword);


    // 아이디와 쿠폰번호가 같은 녀석을 찾아서 있다면 여기에 set을해줘서 등록해주자
    SellerCoupon findByIdAndCouponPassword(int id, String CouponPassword);

    void deleteByUserModel(UserModel userModel);

}
