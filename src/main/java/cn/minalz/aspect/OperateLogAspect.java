package cn.minalz.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 自定义日志切面
 */
@Aspect
@Component
@Slf4j
public class OperateLogAspect {

    @Pointcut(value = "@annotation(operateLog) && args(obj)")
    public void pointCut(OperateLog operateLog, Object obj) {

    }

    @AfterThrowing(pointcut = "pointCut(operateLog,obj)", throwing = "ex")
    public void afterThrowing(OperateLog operateLog, Exception ex, Object obj) {
        if (operateLog.businessType().equalsIgnoreCase("operator:bussiness")) {
            log.info("only test");
//            ParamObj paramObj = (ParamObj) obj;
        }
    }

}

