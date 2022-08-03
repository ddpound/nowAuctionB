package com.auction.nowauctionb.admin.repository;

import com.auction.nowauctionb.admin.model.AdminBoardCategory;
import com.auction.nowauctionb.admin.model.IntegrateBoardModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntegrateBoardRepository extends JpaRepository<IntegrateBoardModel,Integer> {


    List<IntegrateBoardModel> findAllByAdminBoardCategory(AdminBoardCategory adminBoardCategory);


}
