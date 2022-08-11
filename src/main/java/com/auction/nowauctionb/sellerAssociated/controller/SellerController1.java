package com.auction.nowauctionb.sellerAssociated.controller;

import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.service.SellerService1;
import com.auction.nowauctionb.sellerAssociated.service.ShoppingMallService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "seller")
@RestController
public class SellerController1 {

    // 방만들기
    // 내 쇼핑몰,공간 만들기
    // 게시판 글 작성하기

    private final SellerService1 sellerService1;

    private final ShoppingMallService1 shoppingMallService1;

    // 채팅방 개설
    @PostMapping("make-room")
    public ResponseEntity makeRoom(){

        return null;
    }

    @GetMapping("check-mall")
    public ResponseEntity shoppingMallCheck(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        return new ResponseEntity(sellerService1.checkShoppingMall(principalDetails),HttpStatus.OK);
    }

    // 쇼핑몰 만들기
    @PostMapping("make-shopping-mall")
    public ResponseEntity makeMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                             @RequestParam("thumbnail") MultipartFile multipartFile,
                                             @RequestParam("explantion") String shoppingMallExplanation,
                                             Authentication authentication,
                                             HttpServletRequest request) throws IOException {

        int resultNum = shoppingMallService1.SaveNewShoppingMall(authentication ,
                multipartFile,
                shoppingmallName,
                shoppingMallExplanation,request);

        if(resultNum == 1){
            return new ResponseEntity("", HttpStatus.OK);
        } else if (resultNum == -2) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("Already-ShoppingMall-Name",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("modify-shopping-mall")
    public ResponseEntity modifyMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                               @RequestParam(value = "thumbnail",required = false) MultipartFile multipartFile,
                                               @RequestParam(value = "thumbnail2",required = false) String urlFilePath,
                                               @RequestParam("explantion") String shoppingMallExplanation,
                                               Authentication authentication,
                                               HttpServletRequest request) throws IOException {

        int resultNum = shoppingMallService1.modifyShoppingMall(authentication ,
                multipartFile,
                shoppingmallName,
                shoppingMallExplanation,
                urlFilePath,
                request);

        if(resultNum == 1){
            return new ResponseEntity("", HttpStatus.OK);
        } else if (resultNum == -2) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("Already-ShoppingMall-Name", HttpStatus.OK);
        } else if (resultNum == -3) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("user-have-not-ShoppingMall", HttpStatus.OK);
        } else {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }

    // 제품등록
    @PostMapping(value = "register-product")
    public ResponseEntity registerProduct(@RequestParam("productName") String productName,
                                          @RequestParam("productPrice") String productPrice,
                                          Authentication authentication,
                                          HttpServletRequest request){

        int resultNum = shoppingMallService1.saveProduct(authentication,productName ,productPrice,request);

        return new ResponseEntity("good-product", HttpStatus.OK);
    }


}
