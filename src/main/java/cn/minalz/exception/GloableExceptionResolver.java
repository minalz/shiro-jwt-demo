package cn.minalz.exception;

import cn.minalz.common.CommonResult;
import cn.minalz.enums.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常处理器
 */
@RestControllerAdvice
public class GloableExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(GloableExceptionResolver.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = GloableException.class)
    public CommonResult bizExceptionHandler(GloableException e) {
        log.error(e.getMessage(), e);
        return CommonResult.defineError(e);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return CommonResult.otherError(ErrorEnum.INTERNAL_SERVER_ERROR);

    }

}
