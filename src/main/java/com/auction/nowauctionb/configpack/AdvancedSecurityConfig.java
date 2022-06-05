package com.auction.nowauctionb.configpack;


// 보안에 관한 설정파일, 로그인시 필터부분을 전부 관할

import com.auction.nowauctionb.configpack.jwtconfig.JWTUtil;
import com.auction.nowauctionb.configpack.jwtconfig.LoginFilterJWTUtil;
import com.auction.nowauctionb.filter.JWTCheckFilter;
import com.auction.nowauctionb.filter.JWTLoginFilter;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import com.auction.nowauctionb.loginjoin.service.TokenJoinService;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor // 요즘 autowierd 대신쓰기위해 나온것
public class AdvancedSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsConfig;

    private final UserModelRepository userModelRepository;

    private TokenJoinService tokenJoinService;

    // 여기서 등록 나머지에서는 등록하는걸 권장하지않음
    @Bean
    public LoginFilterJWTUtil loginFilterJWTUtil(){
        return new LoginFilterJWTUtil();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // IOC 에서 로직 짤때 사용
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // session은 안하는걸로 , csrf 끄기
        http
                // cors config 클래스로 설정을 줄꺼여서 그냥 이대로 주석처리
                //.cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsConfig) // @CrossOrigin (인증 x), 시큐리티 필터 등록 인증
                // 유저 패스워드 값으로 로그인을 진행 안함 , 폼로그인 x
                .formLogin().disable()
                // 기본적인 http 로그인방식도 사용하지않는다.
                .httpBasic().disable()
                // 위의 필터를 껐기 때문에 아래 필터를 재추가
                // WebSecurityConfigurerAdapter 가 들고있음
                // 뉴가 아니라 아래처럼 해줘야함, JWT는 IOC에서 객체 관리를 하지않기에
                .addFilter(new JWTLoginFilter(authenticationManager(), loginFilterJWTUtil())) // AuthenticationManager를 던져줘야함
                //
                .addFilter(new JWTCheckFilter(authenticationManager(),loginFilterJWTUtil(), userModelRepository))
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
