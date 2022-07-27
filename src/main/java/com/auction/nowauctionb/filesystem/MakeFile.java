package com.auction.nowauctionb.filesystem;


import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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

    // 해당것은 한개의 파일만 입력했을때
    public Map<Integer,String> makeFileImage(UserModel userModel,
                                MultipartFile multipartFile,
                                HttpServletRequest request){

        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();

        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 포맷 적용
        String formatedNow = now.format(formatter);


        // 현재시간 + 작성자 + 쇼핑몰이름 + uuid

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 명 가져오기

        String savedFileName = "Thumbnail"+"-"+ UUID.randomUUID()+extension;

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

        // 배포때는 수정해야할 듯
        String mainurl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";

        // 총 두가지를 저장해내야하니 두개를 리턴
        // 한가지는 일단 http를 통한 정적 자료를 반환할수있는 경로 주소
        // 나머지 한가지는 파일 위치 주소

        Map<Integer, String> returnPathNams = new HashMap<>();

        returnPathNams.put(1, mainurl+saveFolderName.substring(saveFolderName.indexOf("Jang")) + savedFileName);
        returnPathNams.put(2, saveFolderName + savedFileName);

        // 파일저장
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        return returnPathNams;
    }

    // 글을 작성할 때 임시파일들을 저장해주기
    public JsonObject makeTemporaryfiles(MultipartFile multipartFile,
                                         UserModel userModel,
                                         HttpServletRequest request){

        JsonObject jsonObject = new JsonObject();
        String temporarySavePath = AllStaticStatus.temporaryImageFiles+File.separator+userModel.getUserId();

        // 오리지널 파일 명에 붙어있는 확장자를 떼서 예) .jpeg .png 등만 떼서 다시 이름을 정해주는 작업
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String saveFileName = userModel.getUserId() +"-"+ UUID.randomUUID() + extension;
        File targetFile = new File(temporarySavePath + saveFileName);

        // url주소를 보내줘야하니
        String mainurl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            jsonObject.addProperty("url", mainurl+AllStaticStatus.temporaryImageFiles+File.separator+userModel.getUserId());
            jsonObject.addProperty("responseCode", "success");
            multipartFile.getInputStream().close();

        }catch (IOException e){
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }
}
