package com.auction.nowauctionb.sellerAssociated.controller;

import com.auction.nowauctionb.admin.model.AdminBoardCategory;
import com.auction.nowauctionb.admin.model.IntegrateBoardModel;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.ShoppinMallModel;
import com.auction.nowauctionb.sellerAssociated.service.SellerService1;
import com.auction.nowauctionb.sellerAssociated.service.ShoppingMallService1;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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


    private final MakeFile makeFile;

    // 채팅방 개설
    @PostMapping("make-room")
    public ResponseEntity makeRoom() {

        return null;
    }

    @GetMapping("check-mall")
    public ResponseEntity shoppingMallCheck(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        return new ResponseEntity(sellerService1.checkShoppingMall(principalDetails), HttpStatus.OK);
    }

    // 쇼핑몰 만들기
    @PostMapping("make-shopping-mall")
    public ResponseEntity makeMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                             @RequestParam("thumbnail") MultipartFile multipartFile,
                                             @RequestParam("explantion") String shoppingMallExplanation,
                                             Authentication authentication,
                                             HttpServletRequest request) throws IOException {

        int resultNum = shoppingMallService1.SaveNewShoppingMall(authentication,
                multipartFile,
                shoppingmallName,
                shoppingMallExplanation, request);

        if (resultNum == 1) {
            return new ResponseEntity("", HttpStatus.OK);
        } else if (resultNum == -2) {
            // 중복된 쇼핑몰 이름
            return new ResponseEntity("Already-ShoppingMall-Name", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("modify-shopping-mall")
    public ResponseEntity modifyMyShoppingMall(@RequestParam("shoppingMallName") String shoppingmallName,
                                               @RequestParam(value = "thumbnail", required = false) MultipartFile multipartFile,
                                               @RequestParam(value = "thumbnail2", required = false) String urlFilePath,
                                               @RequestParam("explantion") String shoppingMallExplanation,
                                               Authentication authentication,
                                               HttpServletRequest request) throws IOException {

        int resultNum = shoppingMallService1.modifyShoppingMall(authentication,
                multipartFile,
                shoppingmallName,
                shoppingMallExplanation,
                urlFilePath,
                request);

        if (resultNum == 1) {
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
                                          HttpServletRequest request) {

        int resultNum = shoppingMallService1.saveProduct(authentication, productName, productPrice, request);

        return new ResponseEntity("good-product", HttpStatus.OK);
    }

    // 임시파일 삭제
    @PostMapping(value = "temporary-image-save", produces = "application/json")
    public JsonObject boardImageTemporarySave(
            @RequestParam("file") MultipartFile multipartFile,
            Authentication authentication,
            HttpServletRequest request) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        return makeFile.makeTemporaryfiles(multipartFile, principalDetails.getUserModel(), request);
    }

    // 파일에 있는 파일이름을 받아온 content 즉 lob형태의 문자열에 검색해보고 결과없으면 옮기지 않기
    // 사용하지 않는 이미지파일을 옮기면 더미데이터가 생성되는 꼴


    @PostMapping(value = "save-product")
    public ResponseEntity saveProduct(@RequestParam("productname") String productname,
                                      @RequestParam("productprice") String productprice,
                                      @RequestParam("content") String content,
                                      @RequestParam(value="thumbnail1", required=false) MultipartFile file1,
                                      @RequestParam(value="thumbnail2", required=false) MultipartFile file2,
                                      @RequestParam(value="thumbnail3", required=false) MultipartFile file3,
                                      Authentication authentication,
                                      HttpServletRequest request) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("productname" + productname);
        System.out.println("productprice" +productprice);
        System.out.println("content" +content);
        System.out.println("thumbnail1" + file1);
        System.out.println("thumbnail2" + file2);
        System.out.println("thumbnail3" + file3);




//        adminService1.saveAnnouncementBoardImageFIle(principalDetails.getUserModel().getUserId(),boardData.get("content"));
//        adminService1.saveAnnouncementBoard(
//                IntegrateBoardModel.builder()
//                        .title(boardData.get("title"))
//                        .Content(boardData.get("content"))
//                        .userModel(principalDetails.getUserModel())
//                        .adminBoardCategory(AdminBoardCategory.Announcemnet)
//                        .build(),
//                request
//        );


        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
