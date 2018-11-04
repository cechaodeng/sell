package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回到前端的对象
 * @author Kent
 * @date 2017-10-27.
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -4942302575068906789L;

    private Integer code;

    private String msg;

    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
        this.data = data;
    }

    public ResultVO(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
        this.data = null;
    }
}
