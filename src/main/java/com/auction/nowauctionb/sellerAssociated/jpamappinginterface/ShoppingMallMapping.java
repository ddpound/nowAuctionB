package com.auction.nowauctionb.sellerAssociated.jpamappinginterface;


// JPA의 또다른 사용법


import java.sql.Timestamp;

public interface ShoppingMallMapping {
    int getId();
    String getShoppingMallName();
    String getShppingMallExplanation();
    String getThumbnailUrlPath();
    String getThumbnailFilePath();
    Timestamp getCreateShoppinMall();
    Timestamp getModifyShoppinMall();


    Whatever getUserModel();

    interface Whatever{
        int getUserId();
        String getUsername();
        String getPicture();
    }
}
