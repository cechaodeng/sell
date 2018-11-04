package com.imooc.repository;

import com.imooc.dataobject.SellerInfo;
import com.imooc.exception.SellException;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Kent
 * @date 2017-11-09.
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openid);
}
