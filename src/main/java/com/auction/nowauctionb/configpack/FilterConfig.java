package com.auction.nowauctionb.configpack;


import com.auction.nowauctionb.filter.TestFilter1;
import com.auction.nowauctionb.filter.TestFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TestFilter1> filterTest(){
        FilterRegistrationBean<TestFilter1> bean = new FilterRegistrationBean<>(new TestFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 전에 했던 오더 코드 가장 먼저해라 0이니까

        return bean;
    }

    @Bean
    public FilterRegistrationBean<TestFilter2> secondTest(){
        FilterRegistrationBean<TestFilter2> bean = new FilterRegistrationBean<>(new TestFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // 전에 했던 오더 코드 가장 먼저해라 1이니까

        return bean;
    }



}
