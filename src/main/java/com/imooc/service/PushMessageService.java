package com.imooc.service;

import com.imooc.dto.OrderDTO;

import javax.persistence.criteria.Order;

/**
 * @author Kent
 * @date 2017-11-10.
 */
public interface PushMessageService {
    void orderStatus(OrderDTO orderDTO);
}
