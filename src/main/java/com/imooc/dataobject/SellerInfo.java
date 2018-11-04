package com.imooc.dataobject;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Kent
 * @date 2017-11-09.
 */
@Data
@Entity//可以对应数据库中的?
public class SellerInfo {
    @Id
    private String id;

    private String username;

    private String password;

    private String openid;
}
