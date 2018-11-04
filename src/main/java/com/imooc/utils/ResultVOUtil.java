package com.imooc.utils;

import com.imooc.VO.ResultVO;
import com.imooc.enums.ResultEnum;

/**
 * @author Kent
 * @date 2017-10-27.
 */
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        return new ResultVO(ResultEnum.SUCCESS,object);
    }

    public static ResultVO success() {
        return new ResultVO(ResultEnum.SUCCESS);
    }

    public static ResultVO error() {
        return new ResultVO(ResultEnum.ERROR);
    }

    public static ResultVO error(Integer code, String msg) {
        return new ResultVO(code, msg);
    }
}
