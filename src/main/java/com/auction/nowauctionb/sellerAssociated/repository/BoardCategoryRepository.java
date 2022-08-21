package com.auction.nowauctionb.sellerAssociated.repository;

import com.auction.nowauctionb.sellerAssociated.model.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Integer> {
}
