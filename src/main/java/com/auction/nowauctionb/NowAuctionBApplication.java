package com.auction.nowauctionb;

import com.auction.nowauctionb.allstatic.AllStaticStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class NowAuctionBApplication {

    public static void main(String[] args) {
        SpringApplication.run(NowAuctionBApplication.class, args);
    }

}
