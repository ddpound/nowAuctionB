package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.admin.model.AuthNames;
import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.ProductModelRepository;
import com.auction.nowauctionb.sellerAssociated.repository.ShoppingMallModelRepositry;
import lombok.RequiredArgsConstructor;


import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallService1 {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final ProductModelRepository productModelRepository;

    @Transactional
    public int SaveNewShoppingMall(Authentication authentication,
                                   MultipartFile multipartFile,
                                   String shoppingMallName,
                                   String shoppingMallExplanation,
                                   HttpServletRequest request){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 12길이보다 길면
        if(shoppingMallName.length() > 12){
            return -5;
        }


        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByShoppingMallName(shoppingMallName);

        if(shoppingMallModel != null ){
            // 중복된 쇼핑몰이름
            return -2;
        }

        // 파일저장
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        Map<Integer,String> fileNames =makeFile.makeFileImage(principalDetails.getUserModel(), multipartFile,request);


        ShoppingMallModel shoppingMallModelSave =
                ShoppingMallModel.builder()
                        .shoppingMallName(shoppingMallName)
                        .shoppingMallExplanation(shoppingMallExplanation)
                        .thumbnailUrlPath(fileNames.get(1))
                        .thumbnailFilePath(fileNames.get(2))
                        .userModel(principalDetails.getUserModel())
                        .build();

        shoppingMallModelRepositry.save(shoppingMallModelSave);

        return 1;
    }

    // 수정할때 해야할것
    // 사진이 추가됐다면 해당 사진은 삭제하고
    // 새사진을 올려야함
    @Transactional
    public int modifyShoppingMall(Authentication authentication,
                                   MultipartFile multipartFile,
                                   String shoppingMallName,
                                   String shoppingMallExplanation,
                                   String urlFilePath,
                                   HttpServletRequest request){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 12길이보다 길면
        if(shoppingMallName.length() > 12){
            return -5;
        }


        // 해당유저가 이미 쇼핑몰이 있다는 가정 하에
        // 영속화
        ShoppingMallModel shoppingMallModel1ByUserModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        // 사진이 달라졌을경우 삭제 후 추가
        // 그대로라면 그냥 그대로
        if(shoppingMallModel1ByUserModel == null ) {
            // 해당 유저가 가진 쇼핑몰이 없을 때
            return -3;
        }

        // 비어있다면 아마 새로운 파일을 보낸것일테니 삭제
        if(urlFilePath == null){
            // 새로운 파일을 저장하기 앞서 먼저 삭제해야함 해당 사진은
            // 파일 경로를 불러와 그대로 삭제
            makeFile.filePathImageDelete(shoppingMallModel1ByUserModel.getThumbnailFilePath());
            // 새로운 파일저장 입니다.
            // 1. url 사진 경로
            // 2. 컴퓨터 사진 파일 경로
            Map<Integer,String> fileNames =makeFile.makeFileImage(principalDetails.getUserModel(), multipartFile,request);
            shoppingMallModel1ByUserModel.setThumbnailUrlPath(fileNames.get(1));
            shoppingMallModel1ByUserModel.setThumbnailFilePath(fileNames.get(2));
        }

        // 아니면 냅둔다



        // 더티체킹
        shoppingMallModel1ByUserModel.setShoppingMallName(shoppingMallName);
        shoppingMallModel1ByUserModel.setShoppingMallExplanation(shoppingMallExplanation);



        return 1;
    }
    @Transactional
    public int saveProduct(Authentication authentication,
                           String productName,
                           int productPrice,
                           int productquantity,
                           String content,
                           List<MultipartFile> fileList,
                           HttpServletRequest request){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 영속화
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        StringBuilder productFilePath = new StringBuilder();
        StringBuilder productUrlPath = new StringBuilder();

        // 파일저장 (make file)
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로

        // 받아온 썸네일 길이부터
        if(fileList.size() > 0){

            if(fileList.size() > 3 ){

                return -4; // 3개 이상의 썸네일
            }

            Map<Integer,String> returnPathName = new HashMap<>();

            for (MultipartFile file : fileList
                 ) {
                 returnPathName = makeFile.makeFileImage(principalDetails.getUserModel(), file,request);

                 productUrlPath.append(returnPathName.get(1)).append(",");
                 productFilePath.append(returnPathName.get(2)).append(",");
            }


        }else{
            return -5 ; // no thumbnail
        }

        // 위 필터로 썸네일은 무조건 존재한다는 조건에

        // 주의 반드시 옮기고 난다음에 content를 수정할것
        int makeFileResult = makeFile.saveMoveImageFiles(principalDetails.getUserModel().getUserId(),content, AuthNames.Seller);
        makeFile.deleteTemporary(principalDetails.getUserModel().getUserId());

        if(makeFileResult == -1){
            return -3; // 사진 10을 넘겨버림
        }

        ProductModel productModel = ProductModel.builder()
                .productName(productName)
                .productPrice(productPrice)
                .productQuantity(productquantity)
                .pictureFilePath(productFilePath.toString())
                .content(makeFile.changeContentImageUrlPath(principalDetails.getUserModel().getUserId(),content,request))
                .pictureUrlPath(productUrlPath.toString())
                .shoppingMall(shoppingMallModel)
                .build();

        try {
            productModelRepository.save(productModel);

        }catch (Exception e){
            log.info(e);
            return -1; // 단순 에러
        }


        // 문제 없다면 반환 1, 여기까지왔다면 임시파일 삭제
        // 임시파일 삭제
        makeFile.deleteTemporary(principalDetails.getUserModel().getUserId());
        return 1;
    }

    @Transactional(readOnly = true)
    public List<ShoppingMallModel> findAllShoppingMallList(){

        List<ShoppingMallModel> findShoppingMallModel = shoppingMallModelRepositry.findAll();


        if(findShoppingMallModel.size() > 0){
            for (int i=0 ; i < findShoppingMallModel.size(); i++) {
                findShoppingMallModel.get(i).getUserModel().setPassword("");
            }
        }

        return findShoppingMallModel;
    }

    @Transactional(readOnly = true)
    public void saveProductImageFIle(int userproductdId,String content){

        // 파일 관련 부분
        makeFile.saveMoveImageFiles(userproductdId, content, AuthNames.Seller);
        makeFile.deleteTemporary(userproductdId);

    }
    @Transactional
    public void saveProduct(Authentication authentication, ProductModel productModel, HttpServletRequest request){

        PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();

        // 배포때는 수정해야할 듯
        String mainurl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";

        // http://localhost:5000/Temporrary_files/1/ 까지의 파일경로를 변경해주자
        String changeTargetFolderPath
                = mainurl+ AllStaticStatus.temporaryImageFiles.substring(
                AllStaticStatus.temporaryImageFiles
                        .indexOf("Temporary"))
                + principalDetails.getUserModel().getUserId()+"/";

        // 앞의 C나 home 루트를 제외시킴
        String changeFolerPath = mainurl+AllStaticStatus.saveImageFileRoot
                .substring(AllStaticStatus.saveImageFileRoot.indexOf("Jang"))+makeFile.nowDate()+"/";

        // 바꿔줘야함 문자열 받은걸
        String changeBoardContent = productModel.getContent().replace(changeTargetFolderPath,changeFolerPath);
        productModel.setContent(changeBoardContent);

        productModelRepository.save(productModel);
    }
}
