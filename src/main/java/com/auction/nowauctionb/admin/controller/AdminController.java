package com.auction.nowauctionb.admin.controller;

import com.auction.nowauctionb.admin.service.AdminService1;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import com.auction.nowauctionb.sellerAssociated.repository.SellerCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AdminService1 adminService1;

    @GetMapping(value = "info")
    public String setAdminUrl(){




        return "successAdmin";
    }

    @PostMapping(value = "make-coupon")
    public ResponseEntity makeCoupon(@RequestParam(required = false)String couponNumber){


        adminService1.makeCoupon(couponNumber);


        return new ResponseEntity("success make coupon",HttpStatus.OK);
    }




}
