package com.auction.nowauctionb.admin.controller;

import com.auction.nowauctionb.admin.service.AdminService1;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import com.auction.nowauctionb.sellerAssociated.repository.SellerCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AdminService1 adminService1;

    @GetMapping(value = "info")
    public String setAdminUrl(){


        adminService1.makeCoupon();

        return "successAdmin";
    }




}
