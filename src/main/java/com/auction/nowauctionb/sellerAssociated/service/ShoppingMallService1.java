package com.auction.nowauctionb.sellerAssociated.service;

import com.auction.nowauctionb.admin.model.AuthNames;
import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.configpack.auth.PrincipalDetails;
import com.auction.nowauctionb.filesystem.MakeFile;
import com.auction.nowauctionb.sellerAssociated.model.BoardCategory;
import com.auction.nowauctionb.sellerAssociated.model.CommonModel;
import com.auction.nowauctionb.sellerAssociated.model.ProductModel;
import com.auction.nowauctionb.sellerAssociated.model.ShoppingMallModel;
import com.auction.nowauctionb.sellerAssociated.repository.BoardCategoryRepository;
import com.auction.nowauctionb.sellerAssociated.repository.CommonModelRepository;
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
import java.util.Optional;


@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallService1 {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final ProductModelRepository productModelRepository;

    private final CommonModelRepository commonModelRepository;

    private final BoardCategoryRepository boardCategoryRepository;


    /**
     * 쇼핑몰을 저장 함수
     * 반환값 1 일때는 올바른 값
     * -2 일때는 이미 있는 쇼핑몰이라는 뜻
     * -5 이면 제목이 12자 이상
     * @param authentication 현재 사용유저가 누구인지 파악
     * @param multipartFile 썸네일
     * @param shoppingMallName 쇼핑몰 이름
     * @param shoppingMallExplanation 쇼핑몰 설명
     * @param request 파일 저장을 위한 url값을 가져오기
     * */
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
                        .filefolderPath(fileNames.get(3))
                        .userModel(principalDetails.getUserModel())
                        .build();

        shoppingMallModelRepositry.save(shoppingMallModelSave);

        return 1;
    }

    /**
     * 쇼핑몰 수정 메소드 이다
     * 사진이 새로 추가됐다면 해당사진은삭제
     * 새사진이 올라온다
     *
     *  리턴값이 -5면 제목이 12자 이상 이라는뜻
     * */
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
            makeFile.folderPathImageDelete(shoppingMallModel1ByUserModel.getFilefolderPath());
            // 새로운 파일저장 입니다.
            // 1. url 사진 경로
            // 2. 컴퓨터 사진 파일 경로
            Map<Integer,String> fileNames =makeFile.makeFileImage(principalDetails.getUserModel(), multipartFile,request);
            shoppingMallModel1ByUserModel.setThumbnailUrlPath(fileNames.get(1));
            shoppingMallModel1ByUserModel.setThumbnailFilePath(fileNames.get(2));
        }


        // 더티체킹
        shoppingMallModel1ByUserModel.setShoppingMallName(shoppingMallName);
        shoppingMallModel1ByUserModel.setShoppingMallExplanation(shoppingMallExplanation);



        return 1;
    }

    /**
     *  제품 저장 함수
     * @param modify true일때 수정, false일대는 일반 save
     * @param ProductID 수정일 때 보드 아이디도 받아와야함 값이 있는지 체크
     *
     * @param productName 제품이름
     * @param productPrice 제품가격
     * @param content 제품 내용
     * @param fileList 썸네일 리스트, 수정일때는 없어도 된다
     *
     * */
    @Transactional
    public int saveProduct(Authentication authentication,
                           Integer ProductID,
                           String productName,
                           int productPrice,
                           int productquantity,
                           String content,
                           List<MultipartFile> fileList,
                           HttpServletRequest request,
                           boolean modify) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        StringBuilder productFilePath = new StringBuilder();
        StringBuilder productUrlPath = new StringBuilder();

        // 주의 반드시 옮기고 난다음에 content를 수정할것
        Map<Integer, String> makeFileResult = makeFile.saveMoveImageFiles(principalDetails.getUserModel().getUserId(), content, AuthNames.Seller);

        if (makeFileResult.get(1) == "-3") {
            return -3; // 사진 10을 넘겨버림
        }

        // 사진을 옮긴후 content 내용의 url경로도 변경
        String changecontent = makeFile.changeContentImageUrlPath(principalDetails.getUserModel().getUserId(), content, makeFileResult.get(2), request);


        // 파일저장 (make file)
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로

        // 썸네일을 받아오고 수정이 아닐때
        if (fileList.size() > 0 && modify) {
            if (fileList.size() > 3) {
                return -4; // 3개 이상의 썸네일
            }

            Map<Integer, String> returnPathName = new HashMap<>();
            // 썸네일 저장하기

            for (MultipartFile file : fileList
            ) {
                returnPathName = makeFile.makeFileImage(principalDetails.getUserModel(), file, request);

                productUrlPath.append(returnPathName.get(1)).append(",");
                productFilePath.append(returnPathName.get(2)).append(",");
            }
        }

        // 수정이 아니고, 리스트가 0일때
        // 썸네일이 없으니깐 -5를 반환
        if(fileList.size() ==0 && !modify){
            return -5;
        }



        // 수정이 참이면서
        // ID가 널이 아닐때, 수정을 진행
        if (modify && ProductID != null) {

            // 수정이니 이미 제품이 있으니 검사를 시도
            // 동시에 영속화
            Optional<ProductModel> productModel = productModelRepository.findById(ProductID);

            // 받아온 썸네일이 있을때
            if (fileList.size() > 0) {

            }

            productModel.get().setProductName(productName);
            productModel.get().setProductQuantity(productquantity);
            productModel.get().setProductPrice(productPrice);
            productModel.get().setContent(changecontent);

        }



        // 영속화
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());




            // 위 필터로 썸네일은 무조건 존재한다는 조건에


            makeFile.deleteTemporary(principalDetails.getUserModel().getUserId());


            ProductModel productModel = ProductModel.builder()
                    .productName(productName)
                    .productPrice(productPrice)
                    .productQuantity(productquantity)
                    .pictureFilePath(productFilePath.toString())
                    .content(changecontent)
                    .filefolderPath(makeFileResult.get(2))
                    .pictureUrlPath(productUrlPath.toString())
                    .shoppingMall(shoppingMallModel)
                    .build();

            try {
                productModelRepository.save(productModel);

            } catch (Exception e) {
                log.info(e);
                return -1; // 단순 에러
            }


            // 문제 없다면 반환 1, 여기까지왔다면 임시파일 삭제
            // 임시파일 삭제
            makeFile.deleteTemporary(principalDetails.getUserModel().getUserId());
            return 1;
        }



    /**
     * 판매자가 판매자 권한을 인증받고
     * 제품 수정을위해 받아내는 제품 메소드
     * */
    public Optional<ProductModel> findProduct(int id){
        return productModelRepository.findById(id);
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

    // 카테고리를 만들었을때 이름이 똑같으면, 알아서 만들어지도록할지..아니면...음..
    @Transactional
    public int saveBoard(Authentication authentication,
                         String title,
                         String content,
                         MultipartFile thumbnail,
                         int categoryId,
                         HttpServletRequest request){

        PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();

        Optional<BoardCategory> findBoardCategory = boardCategoryRepository.findById(categoryId);

        // 파일저장
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        // 3. 폴더 경로
        Map<Integer, String> returnPathNams = makeFile.makeFileImage(principalDetails.getUserModel(),thumbnail,request);

        // 바꾸기전에 먼저이동, 검사를 통해 안쓰는 파일들을 삭제시킬 예정
        makeFile.saveMoveImageFiles(principalDetails.getUserModel().getUserId(),content,AuthNames.Seller);

        // ----------------- 받은 content 를 바꿔주기 경로 ------------------------------//
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
        String changeBoardContent = content.replace(changeTargetFolderPath,changeFolerPath);
        // ----------------- 받은 content 를 바꿔주기 경로 ------------------------------//



        commonModelRepository.save(CommonModel.builder()
                .boardCategory(findBoardCategory.get())
                .userModel(principalDetails.getUserModel())
                .pictureUrlPath(returnPathNams.get(1))
                .pictureFilePath(returnPathNams.get(2))
                .title(title)
                .Content(changeBoardContent)
                .build());


        // 다 저장했으면 삭제
        makeFile.deleteTemporary(principalDetails.getUserModel().getUserId());
        return 1;
    }

    @Transactional(readOnly = true)
    public List<BoardCategory> getBoardCategoryList(Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 현재 접속중인 유저의 쇼핑몰을 검색
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        List<BoardCategory> boardCategoryList = boardCategoryRepository.findAllByShoppingMall(shoppingMallModel);

        if(boardCategoryList.size() > 0){
            for (int i=0 ; i < boardCategoryList.size(); i++) {
                boardCategoryList.get(i).getShoppingMall().getUserModel().setPassword("");
            }
        }


        return boardCategoryList;
    }

    @Transactional
    public int saveBoardCategory(Authentication authentication, String categoryName){

        if(categoryName.contains(" ")){
            return -2; // 공백검사
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        //만약에 이미 있는거라면 세이브 없이 그냥 올바른 값만 보내기
        if(boardCategoryRepository.findByCategoryName(categoryName) != null){
            return 1;
        }


        try {
            boardCategoryRepository.save(BoardCategory
                    .builder()
                    .categoryName(categoryName)
                    .shoppingMall(shoppingMallModel)
                    .build());

        }catch (Exception e){
            log.info("boardCategory saveFail, fail shoppingMall"+ shoppingMallModel.getShoppingMallName());
            return -1; // 특정에러
        }


        return 1;
    }

    @Transactional
    public int modifyBoardCategory(Authentication authentication, String categoryName){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUserModel(principalDetails.getUserModel());

        // 영속화
        BoardCategory boardCategory = boardCategoryRepository.findByShoppingMall(shoppingMallModel);

        try {
            // 더티체킹
            boardCategory.setCategoryName(categoryName);

        }catch (Exception e){
            log.info("boardCategory modify Fail, fail shoppingMall :"+ shoppingMallModel.getShoppingMallName());
            return -1; // 특정에러
        }


        return 1;
    }
}
