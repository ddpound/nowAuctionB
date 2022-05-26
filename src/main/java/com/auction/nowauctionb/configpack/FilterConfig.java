package com.auction.nowauctionb.configpack;


import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.filter.TestFilter1;
import com.auction.nowauctionb.filter.TestFilter2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {


//    @Autowired
//    JWTUtil jwtUtil;

//    @Bean
//    public FilterRegistrationBean<TestFilter1> filterTest(){
//
//        // 스프링 컨텍스트에서 관리하지 못하기 때문에 필터추가 때 담아야함
//        TestFilter1 testFilter1 = new TestFilter1(jwtUtil);
//
//        FilterRegistrationBean<TestFilter1> bean = new FilterRegistrationBean<>(testFilter1);
//        bean.addUrlPatterns("/*");
//        bean.setOrder(0); // 전에 했던 오더 코드 가장 먼저해라 0이니까
//
//        return bean;
//    }

//    @Bean
//    public FilterRegistrationBean<TestFilter2> secondTest(){
//        FilterRegistrationBean<TestFilter2> bean = new FilterRegistrationBean<>(new TestFilter2());
//        bean.addUrlPatterns("/*"); // 이처럼 특정 url도 설정가능
//        bean.setOrder(1); // 전에 했던 오더 코드 가장 먼저해라 1이니까
//
//        return bean;
//    }



}
