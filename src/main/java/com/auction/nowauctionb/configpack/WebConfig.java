package com.auction.nowauctionb.configpack;

import com.auction.nowauctionb.allstatic.AllStaticStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Log4j2
@Configuration
@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // OS체크 부분
        AllStaticStatus.osName = System.getProperty("os.name").toLowerCase();

        if(AllStaticStatus.osName .contains("win")){
            log.info("Now Os Name is window");
            AllStaticStatus.saveImageFileRoot =  "C:"+ File.separator+"Jang_SaveImage"+ File.separator;

        }else{
            log.info("Now Os Name is linux");
            AllStaticStatus.saveImageFileRoot = "/home/youseongjung/Templates/Jang_SaveImage/";
        }

        if(AllStaticStatus.osName.contains("win")){
            registry.addResourceHandler("/Jang_SaveImage/**")
                    .addResourceLocations("file:///C:/Jang_SaveImage/");
            // 이렇게 리소스 핸들러 추가도 가능한듯 임시 파일, 업로드시 업로드파일 두개를 만들어야하니 알아두자
            // Confirm_Save (확정 저장 파일 이름)
            registry.addResourceHandler("/Confirm_SaveImage/**")
                    .addResourceLocations("file:///C:/Confirm_SaveImage/");
        }else{
            // 리눅스쪽 애들 경로
            registry.addResourceHandler("/Jang_SaveImage/**")
                    .addResourceLocations("file:///home/youseongjung/Templates/Jang_SaveImage/");
            registry.addResourceHandler("/Confirm_SaveImage/**")
                    .addResourceLocations("file:///home/youseongjung/Templates/Confirm_SaveImage/");
        }

    }

}
