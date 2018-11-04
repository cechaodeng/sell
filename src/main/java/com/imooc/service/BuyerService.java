package com.imooc.service;

import com.imooc.dto.OrderDTO;


/**
 * @author Kent
 * @date 2017-11-02.
 */
public interface BuyerService {
    OrderDTO findOrderOne(String openid,String orderId);

    OrderDTO cancelOrder(String openid,String orderId);
}
