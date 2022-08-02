package com.auction.nowauctionb.admin.service;


import com.auction.nowauctionb.admin.model.AdminBoardCategory;
import com.auction.nowauctionb.admin.model.IntegrateBoardModel;
import com.auction.nowauctionb.admin.repository.IntegrateBoardRepository;
import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auction.nowauctionb.sellerAssociated.frontmodel.SellerCouponFront;
import com.auction.nowauctionb.sellerAssociated.model.SellerCoupon;
import com.auction.nowauctionb.sellerAssociated.repository.SellerCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class AdminService1 {

    private final UserModelRepository userModelRepository;

    private final SellerCouponRepository sellerCouponRepository;

    private final IntegrateBoardRepository integrateBoardRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MakeFile makeFile;

    @Value("${jangadmin.secreyKey}")
    private String adminPassword;



    //체크 필터에서 가져온 authentication 값을 디비에 넣고 비밀번호도 받아와야함
    @Transactional
    public int giveAdmin(Authentication authentication,Map<String,Object> password){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        //저장해둔 비밀번호가 같다면
        if(adminPassword.equals(password.get("password"))){
            //  db에서 세팅을 바꿔주자

            // 영속성 , 더티체킹
            UserModel userModel = userModelRepository.findByUsername(principalDetails.getUsername());
            userModel.setRoles("ROLE_USER,ROLE_SELLER,ROLE_ADMIN");
            return 1;

        }else{

            return -1;
        }


    }


    // DB에 판매자 쿠폰 등록을 만들어주는 서비스
    @Transactional
    public int makeCoupon(String num){

        int length = 15;
        boolean useLetters = true;
        boolean useNumbers = true;


        if(num == null){
            num = "1";
        }

        int newNum = Integer.parseInt(num);

        List<SellerCoupon> listSeller = new ArrayList<>();

        for (int i = 0; i < newNum ; i++) {
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);


            listSeller.add(SellerCoupon.builder().couponPassword(generatedString).build());
        }

        sellerCouponRepository.saveAll(listSeller);


        return 1;
    }

    // 삭제할 때 유저 권환도 취소해야함
    @Transactional
    public int deleteCoupon(int id){

        // 영속화
        Optional<SellerCoupon> sellerCoupon = sellerCouponRepository.findById(id);

        if(sellerCoupon.get().getUserModel() != null ){
            // 더티체킹
            sellerCoupon.get().getUserModel().setRoles("ROLE_USER");
        }


        sellerCouponRepository.deleteById(id);
        return 1;
    }


    // 따로 담아서 줘야함
    @Transactional(readOnly = true)
    public List<SellerCouponFront> findAllCoupon(){

        List<SellerCoupon> resultCouponlist = sellerCouponRepository.findAll();

        ArrayList<SellerCouponFront> sellerCouponFrontsArray = new ArrayList<>();

        
        for (SellerCoupon list : resultCouponlist
             ) {
            if(list != null && list.getUserModel() != null){
                sellerCouponFrontsArray.add(SellerCouponFront
                        .builder()
                        .id(list.getId())
                        .couponCode(list.getCouponPassword())
                        .userId(list.getUserModel().getUserId())
                        .userName(list.getUserModel().getUsername())
                        .build());
            }else if(list != null){
                sellerCouponFrontsArray.add(SellerCouponFront
                        .builder()
                        .id(list.getId())
                        .couponCode(list.getCouponPassword())
                        .build());
            }

        }

        return sellerCouponFrontsArray;
    }

    // 유저의 아이디이자 폴더 이름 가져온다는 뜻
    @Transactional(readOnly = true)
    public void saveAnnouncementBoardImageFIle(int userAndBoardId){

        // 파일 관련 부분
        makeFile.saveMoveImageFiles(userAndBoardId);
        makeFile.deleteTemporary(userAndBoardId);

    }
    @Transactional
    public void saveAnnouncementBoard(IntegrateBoardModel boardModel, HttpServletRequest request){

        // 배포때는 수정해야할 듯
        String mainurl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";

        // http://localhost:5000/Temporrary_files/1/ 까지의 파일경로를 변경해주자
        String changeTargetFolderPath
                = mainurl+AllStaticStatus.temporaryImageFiles.substring(
                        AllStaticStatus.temporaryImageFiles
                                .indexOf("Temporary"))
                + boardModel.getUserModel().getUserId()+"/";

        // 앞의 C나 home 루트를 제외시킴
        String changeFolerPath = mainurl+AllStaticStatus.saveImageFileRoot
                .substring(AllStaticStatus.saveImageFileRoot.indexOf("Jang"))+"/"+makeFile.nowDate()+"/";

        // 바꿔줘야함 문자열 받은걸
        String changeBoardContent = boardModel.getContent().replace(changeTargetFolderPath,changeFolerPath);
        boardModel.setContent(changeBoardContent);

        integrateBoardRepository.save(boardModel);
    }

    // 카테고리별로 가져올수 있는 통합 메소드
    @Transactional(readOnly = true)
    public List<IntegrateBoardModel> findAllAndCategoryBoard(AdminBoardCategory adminBoardCategory){

        return integrateBoardRepository.findAllByBoardCategory(adminBoardCategory);
    }

    @Transactional(readOnly = true)
    public Optional<IntegrateBoardModel> findAnnouncementBoard(int boardId){

        return integrateBoardRepository.findById(boardId);
    }

}
