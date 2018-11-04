package com.imooc.service;

import com.imooc.dataobject.SellerInfo;

/**
 * @author Kent
 * @date 2017-11-09.
 */
public interface SellerService {
    /**
     * 通过openId查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
