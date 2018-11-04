package com.imooc.enums;

import lombok.Getter;

/**
 * @author Kent
 * @date 2017-10-27.
 */
@Getter
public enum OrderStatusEnum implements CodeEnum {
    NEW(0,"新订单"),
    FINISHED(1,"完结"),
    CANCEL(2,"已取消")
    ;
    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取一个枚举对象
     * 同样,也有了更好的方法代替,在EnumUtil里面的getCode
     * @param code
     * @return
     */
    /*public static OrderStatusEnum getOrderStatusEnum(Integer code) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getCode().equals(code)) {
                return orderStatusEnum;
            }
        }
        return null;
    }*/
    /**
     * 这种方法应该也可以,只不过逻辑上不太通
     * @param code
     */
    OrderStatusEnum(Integer code) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getCode().equals(code)) {
                this.code = code;
                this.message = orderStatusEnum.getMessage();
                break;
            }
        }
    }
}
