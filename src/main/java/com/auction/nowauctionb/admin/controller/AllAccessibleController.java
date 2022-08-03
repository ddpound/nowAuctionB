package com.auction.nowauctionb.admin.controller;

import com.auction.nowauctionb.admin.model.AdminBoardCategory;
import com.auction.nowauctionb.admin.model.IntegrateBoardModel;
import com.auction.nowauctionb.admin.service.AdminService1;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

// 모두가 접근가능한  Admin Controller

@RequiredArgsConstructor
@RestController
public class AllAccessibleController {

    private final AdminService1 adminService1;


    @GetMapping(value = "find-announcement-board/{boardId}")
    public  ResponseEntity<Optional<IntegrateBoardModel>> findAnnuncementBoard(
            HttpServletRequest request,
            @PathVariable int boardId
    ){

        return new ResponseEntity<>(adminService1.findAnnouncementBoard(boardId), HttpStatus.OK);
    }

    @GetMapping(value = "find-all-announcement-board")
    public ResponseEntity<List<IntegrateBoardModel>> findAllAnnouncementBoard(
            HttpServletRequest request
    ){


        return new ResponseEntity<>(adminService1.findAllAndCategoryBoard(AdminBoardCategory.Announcemnet), HttpStatus.OK);
    }



}
