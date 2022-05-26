package com.auction.nowauctionb.configpack.auth;


import com.auction.nowauctionb.loginjoin.model.UserModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
public class PrincipalDetails implements UserDetails {

    // 현재 User 값은 아래 UserModel이 들고있기 때문에 가져옴
    private UserModel userModel; // 컴포지션

    public PrincipalDetails(UserModel userModel) {
        this.userModel = userModel;
    }

    // 해당 User의 권한을 리턴한다 , ROLE_USER 이런식
    // String type 이여서 아래 new GrantedAuthority 생성자를 이용하면 String 반환이 가능하다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        userModel.getRoleList().forEach(r->{
            authorities.add(()->r);
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }
    // 사용할 username 즉 아이디는 이메일로 하려 합니다.

    // 여기서 중요함 나는 이메일을 반환하도록함
    // 이메일이 시큐리티 계정 기준으로는 유저네임이 됨
    @Override
    public String getUsername() {
        return userModel.getUsername();
    }
    // 만료 유무를 따짐
    @Override
    public boolean isAccountNonExpired() {
        return true; // true일때 만료가 아니다 라는뜻
    }
    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠겼냐 물어봣을 때 아니요
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 너무 오래사용한 비밀번호이면
    }
    @Override
    public boolean isEnabled() {
        // 계정이 활성화 되어있냐, 마지막 로그인날짜에 따라서 휴먼계정등을 할수있음
        return true;
    }
}
