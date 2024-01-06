package com.dzb.exception;

import cn.hutool.core.util.StrUtil;
import com.dzb.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;




@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Result ServiceExceptionError(ServiceException e){
        String code=e.getCode();
        if (StrUtil.isNotBlank(code)) {
            return Result.error(code,e.getMessage());
        }
        return Result.error(e.getMessage());
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionError(Exception e){
        e.printStackTrace();
        return Result.error("系统错误");
    }
}
