package com.auction.nowauctionb.configpack.jwtconfig.rsgoogle;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

public class MyRsPublicKey implements RSAPublicKey {

    // rsa publci exponent 가 jwt 의 e라고 한다
    @Override
    public BigInteger getPublicExponent() {
        return null;
    }

    @Override
    public String getAlgorithm() {
        return "RS256";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return new byte[0];
    }

    // verity signatyre 의 n 이라고함
    @Override
    public BigInteger getModulus() {
        return null;
    }
}
