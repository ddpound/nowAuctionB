package com.auction.nowauctionb.filesystem;


import com.auction.nowauctionb.admin.model.AuthNames;
import com.auction.nowauctionb.allstatic.AllStaticStatus;
import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Component
public class MakeFile {

//    파일 저장
//    유저이름, 날짜
//    동일한 날짜가있을시, 한달 으로 두는게 좋을듯
//    예
//    날짜 폴더 (년도-월/날짜)
//    제목 파일 (제목,아님 썸네일 - UUID)
//    유저아이디-(종류썸네일,제품)-UUID

    public String nowDate(){
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();

        // 포맷 정의
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd");
        // 포맷 적용

        return now.format(formatter1)+"/"+now.format(formatter2);
    }

    // 날짜만 따로
    public String nowDateDay(){
        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();

        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        // 포맷 적용

        return now.format(formatter);
    }


    /** 썸네일등 한개의 이미지파일만 넣을때 쓰는 함수
     * @param userModel 파일이름에 id가 들어감 누구소유인지,
     * @param multipartFile 저장할파일
     * @param request 파일 경로를 위해서
     *
     *  1. url 사진 경로
     *  2. 컴퓨터 사진 파일 경로
     *  3. 데베에 저장할 파일 이름
     * */
    public Map<Integer,String> makeFileImage(UserModel userModel,
                                             MultipartFile multipartFile,
                                             HttpServletRequest request){

        // 현재시간 + 작성자 + 쇼핑몰이름 + uuid

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 명 가져오기

        String savedFileName = userModel.getUserId()+"-"+ UUID.randomUUID()+extension;

        String fileFolderPath = String.valueOf(UUID.randomUUID());

        // 폴더 이름경로명임,
        String saveFolderName = AllStaticStatus.saveImageFileRoot+nowDate()+"/"+fileFolderPath+"/";

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

        // 수정이나 삭제를 위한 폴더
        returnPathNams.put(3, saveFolderName);

        // 파일저장
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        // 3. 데베에 저장할 파일 이름
        return returnPathNams;
    }

    // 글을 작성할 때 임시파일들을 저장해주기
    public JsonObject makeTemporaryfiles(MultipartFile multipartFile,
                                         UserModel userModel,
                                         HttpServletRequest request){

        JsonObject jsonObject = new JsonObject();
        String temporarySavePath = AllStaticStatus.temporaryImageFiles+File.separator+userModel.getUserId()+File.separator;

        String saveFolderName = AllStaticStatus.temporaryImageFiles;

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
            jsonObject.addProperty("url", mainurl
                    +saveFolderName.substring(saveFolderName.indexOf("Temporary"))
                    +userModel.getUserId()
                    +"/"
                    +saveFileName);
            jsonObject.addProperty("responseCode", "success");
            multipartFile.getInputStream().close();

        }catch (IOException e){
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }

    // 원하는 파일들을 모두 Save파일에 옮기고
    // 컨텐츠도 받아와야함 ,
    // content 에 해당 애가 검색안된다면 옮기지 말아야함

    // admin > 사진제한 x
    // seller > 사진 제한 10장

    /**
     * Map integer , String 이며
     * 1 반환값은 결과값 반환
     * 1일때는 올바르고 -3 일때는 옮기는 사진이 10장 이상일 때
     * 2는 등록할 폴더를 뜻함
     * */
    public Map<Integer,String> saveMoveImageFiles(int fileId, String content, AuthNames auth){

        Map <Integer,String> result = new HashMap<>();

        String temporary = AllStaticStatus.temporaryImageFiles+fileId;

        // 현재 시간 가져오는 함수
        String saveFolderRoot = AllStaticStatus.saveImageFileRoot+nowDate()+"/";

        // 해당 게시판이든 관련된 폴더 명을 랜덤으로 가져와주자
        String saveFolderName = String.valueOf(UUID.randomUUID())+"/";

        // 즉 c//jang_save/2022-08/33/블라블라폴더/
        result.put(2, saveFolderRoot+saveFolderName);

        File dir = new File(temporary);
        File[] files = dir.listFiles();

        if(files != null){

            // 관리자가 아니며 옮겨야할 파일이 10장 이상일때
            if (AuthNames.Admin != auth && files.length >10){
                result.put(1, "-3"); // 이미지 파일 10장을 넘김
            }else{
                for(File f : files) {

                    // 파일 이름이 "종류"-"filename"
                    // 이렇게 지정됨

                    String SearchfileName = Integer.toString(fileId);

                    // 아이디로 시작하는 모든 사진들
                    if(f.isFile() && f.getName().startsWith(SearchfileName)) {

                        // 여기서 중요한 부분 content안에 해당 사진이 있는지를 검사
                        // 있을 때만 이동, 그럼 나머지는 삭제되고 본파일에는 게시판에있는것만 옮겨짐
                        if(content.contains(f.getName())){
                            // 이름 변경 -> 파일 이동 -> 오리지널 파일로 이동 url 는 그럼?
                            // DB 이름도 변경해야함 Content 검사해서 변경해서 넣어주기
                            String changeFileName = saveFolderRoot + saveFolderName + f.getName();

                            // 저장할 파일의 경로 (걍로와 이름)
                            File targetFile = new File(changeFileName);
                            try {
                                FileInputStream fileInputStream = new FileInputStream(f); // 저장할 파일
                                FileUtils.copyInputStreamToFile(fileInputStream, targetFile); //저장

                                // 다하면 꼭 닫아줘야하는 의무가 있다
                                fileInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                }
            }



        }else{
            log.info("not image");
        }


        return result; //문제 없다면 1
    }

    /**
     * 임시파일인 temporary를 전부 삭제하는 함수
     * 임시파일은 유저 ID를 통해서 파일을 저장하니 찾아야함
     * @param fileId 임시폴더안의 폴더이자 유저 이름이다.
     * */
    public void deleteTemporary(int fileId){

        // 임시파일 경로에 해당 아이디의 파일이 있음
        String deletePath = AllStaticStatus.temporaryImageFiles + File.separator + fileId;

        // 결로지정
        File folder = new File(deletePath);

        try {
            while(folder.exists()) {
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기
                if(folder_list != null){
                    for (int j = 0; j < folder_list.length; j++) {
                        folder_list[j].delete(); //파일 삭제
                    }

                    if(folder_list.length == 0 && folder.isDirectory()){
                        folder.delete(); //대상폴더 삭제
                    }
                }else{
                    log.info("imageFolder and file is null");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 해당 경로로 이미지 파일 하나만 삭제
     * 보통 쇼핑몰 썸네일 삭제를 위해 만들어둔 메소드 입니다.
     *
     * */
    public void filePathImageDelete(String filePath){

        File file = new File(filePath);

        // exists 존재하다.
        if( file.exists() ){
            if(file.delete()){
                log.info("delete file");
            }else{
                log.info("delete fail");
            }


        }else{
            log.info("This file does not exist");
        }

    }

    /**
     * 폴더를 입력하면 안의 파일 리스트를 받아
     * 모두 삭제한 후 폴더까지 마무리 하는 작업을 거치는 함수
     *
     * */
    public void folderPathImageDelete(String folderPath){

        File folder = new File(folderPath);

        try {
            while(folder.exists()) {
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기

                for (int j = 0; j < folder_list.length; j++) {
                    folder_list[j].delete(); //파일 삭제
                    log.info("file delete");

                }

                if(folder_list.length == 0 && folder.isDirectory()){
                    folder.delete(); //대상폴더 삭제
                    log.info("folder delete");
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    // 이게 검사부분일듯 이름 일치하는것
    // 이름 일치할때만 옮기고 나머지는 삭제하는 과정이 필요함
    public void fileSearchAndDelete(){


    }

    // 주의 content만 수정됨
    // 현재 모든 게시판의 content를 받아와서
    // 파일을 옮기는 도중에 옮긴 파일경로 수정을 위해 따로 만들어둠
    public String changeContentImageUrlPath(int userId, String content,String filefolderPath ,  ServletRequest request){

        String mainurl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";

        // http://localhost:5000/Temporrary_files/1/ 까지의 파일경로를 변경해주자
        // 바꿔줄 경로
        String changeTargetFolderPath = mainurl
                + AllStaticStatus
                .temporaryImageFiles
                .substring(AllStaticStatus.temporaryImageFiles.indexOf("Temporary"))
                +userId+"/";


        // 바뀔 경로명
        // C나 home루트명 제외시키기
        String changeFolderPath = mainurl
                +filefolderPath
                .substring(filefolderPath.indexOf("Jang"));

        return content.replace(changeTargetFolderPath,changeFolderPath);
    }


}
