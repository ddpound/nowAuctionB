package com.auction.nowauctionb.configpack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyResult {


    // 베리파이 검증에 성공했는데 유무
    private boolean success;

    // username을 토큰에 넣어놨기 때문에 넣어둡니다.
    private String username;

}
