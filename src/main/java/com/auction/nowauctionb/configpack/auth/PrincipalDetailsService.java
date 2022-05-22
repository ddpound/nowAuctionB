package com.auction.nowauctionb.configpack.auth;

import com.auction.nowauctionb.loginjoin.model.UserModel;
import com.auction.nowauctionb.loginjoin.repository.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 원래대로라면 loginProcessingUrl("/login") 으로 저장되어 링크를 타고들어온다
// 하지만 지금 해당 프로젝트는 formlogin이 아니며 자동 login도 타고오지않는다

// 아무튼 "/loing"을 타고 들어오면 IOC 가 된
// UserDeatailsService 인터페이스를  상속받은 PrincipalDetailsService 객체를 찾아
// loadUserByUsername 메소드를 실행시켜줍니다.

// 규칙이라고 합니다.

//현재 폼로그인을꺼놔서
// login요청시 제대로 작동을 안한다
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserModelRepository userModelRepository;

    // 시큐리티 세션 = Authentication = UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 제대로 작동하는지 체크
        System.out.println("PrincipalDetailsServiceOnlin");

        UserModel userModel = userModelRepository.findByEmail(username);

        if(userModel != null){

            //  시큐리티 세션 = Authentication( UserDetails) 이렇게 담겨진다
            // 그다음에는
            // 시큐리티 세션 ( Authentication( UserDetails) ) 이렇게 또 담긴다
            // 원래 대로라면
            // 그리고 세션이 만들어지면서 로그인이 완료됩니다.
            return new PrincipalDetails(userModel);
        }

        return null;
    }
}
