package com.auction.nowauctionb.configpack;


// 보안에 관한 설정파일, 로그인시 필터부분을 전부 관할

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Order(1)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdvancedSecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {


        // session은 안하는걸로 , csrf 끄기
        http
                // cors config 클래스로 설정을 줄꺼여서 그냥 이대로 주석처리
                //.cors().disable()
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 유저 패스워드 값으로 로그인을 진행 안함 , 폼로그인 x
                .formLogin().disable()
                // 기본적인 http 로그인방식도 사용하지않는다.
                .httpBasic().disable()
                .authorizeRequests()
                // 로그인시에는 user로 시작하는 모든 url접근은 가능
                //.antMatchers("/user/**").authenticated()
                .antMatchers("/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
                .antMatchers("/seller/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")

                // 위쪽 세개 주소가 아니라면 나머지 주소는 모두 혀용
                .anyRequest().permitAll();




                // 나중에 아래 코드는 꼭 체인 삭제 해줘야함 테스트용도임
                //.antMatchers("/test/**")
                //.permitAll();

    }

}
