package com.auction.nowauctionb.sellerAssociated.jpamappinginterface;


// JPA의 또다른 사용법


import java.sql.Timestamp;

public interface ShoppingMallMapping {
    int getId();
    String getShoppingMallName();
    String getShoppingMallExplanation();
    String getThumbnailUrlPath();
    String getThumbnailFilePath();
    Timestamp getCreateShoppingMall();
    Timestamp getModifyShoppingMall();


    Whatever getUserModel();

    interface Whatever{
        int getUserId();
        String getUsername();
        String getPicture();
    }
}
