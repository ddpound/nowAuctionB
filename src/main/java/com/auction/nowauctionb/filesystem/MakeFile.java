package com.auction.nowauctionb.filesystem;


import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class MakeFile {

//    파일 저장
//    유저이름, 날짜
//    동일한 날짜가있을시, 한달 으로 두는게 좋을듯
//    예
//    날짜 파일 (년도-월-카테고리)
//    제목 파일 (제목,아님 썸네일 - UUID)
//    유저아이디-(종류썸네일,제품)-UUID

    public int makeFileImage(UserModel userModel,
                             MultipartFile multipartFile,
                             String shoppingMallName){

        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();

        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 포맷 적용
        String formatedNow = now.format(formatter);


        // 현재시간 + 작성자 + 쇼핑몰이름 + uuid

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 명 가져오기

        String savedFileName = "Thumbnail-"+shoppingMallName+"-"+ UUID.randomUUID()+ extension;

        String saveFolderName = AllStaticStatus.saveImageFileRoot+formatedNow+"/";

        File targetFile = new File(saveFolderName + savedFileName);


        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            // 설명 읽어보니 닫아놔야한다
            multipartFile.getInputStream().close();
        }catch (IOException e){
            FileUtils.deleteQuietly(targetFile);
        }


        return 1;
    }

}
