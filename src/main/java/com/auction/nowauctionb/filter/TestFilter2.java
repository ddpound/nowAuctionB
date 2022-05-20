package com.auction.nowauctionb.filter;

import javax.servlet.*;
import java.io.IOException;

public class TestFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("테스트 필터2");

        // 필터 체인 두 필터를 통해 반드시 등록해줘야한다
        // 프로세스를 꼭 마저 진행해라
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
