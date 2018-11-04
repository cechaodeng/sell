package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.dataobject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.serializer.Date2LongSerializer;
import com.imooc.utils.EnumUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Kent
 * @date 2017-10-28.
 */
@Data
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 支付状态
     */
    private Integer payStatus;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;

    /**
     * 获得订单状态枚举类型
     * @return
     */
    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        OrderStatusEnum orderStatusEnum = EnumUtil.getByCode(this.orderStatus, OrderStatusEnum.class);
        return orderStatusEnum;
    }

    /**
     * 注解为转成json的时候忽略这两个方法,不要作为属性
     * @return
     */
    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        PayStatusEnum payStatusEnum = EnumUtil.getByCode(this.payStatus, PayStatusEnum.class);
        return payStatusEnum;
    }

}
