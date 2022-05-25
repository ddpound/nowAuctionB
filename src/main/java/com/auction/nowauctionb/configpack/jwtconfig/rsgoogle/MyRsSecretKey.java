package com.auction.nowauctionb.configpack.jwtconfig.rsgoogle;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class MyRsSecretKey implements RSAPrivateKey {

    @Override
    public BigInteger getPrivateExponent() {
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

    @Override
    public BigInteger getModulus() {
        return null;
    }
}
