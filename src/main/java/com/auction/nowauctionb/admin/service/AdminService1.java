package com.auction.nowauctionb.admin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService1 {


    @Value("${jangadmin.secreyKey}")
    private String password;






}
