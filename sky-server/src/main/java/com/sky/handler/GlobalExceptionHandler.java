package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获‘SQLIntegrityConstraintViolationException’异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info("SQLIntegrityConstraintViolationException");

        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();

        //判断是否包含‘Duplicate entry’字段
        if(message.contains("Duplicate entry")){
            return Result.error("用户名重复");
        }else {
            return Result.error("出现未知错误");
        }
    }

}
