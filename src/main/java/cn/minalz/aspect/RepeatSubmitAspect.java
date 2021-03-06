package cn.minalz.aspect;

import cn.minalz.common.ResponseData;
import cn.minalz.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义不重复提交的切面
 */
@Slf4j
@Aspect
@Component
public class RepeatSubmitAspect {

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) {
        int lockSeconds = noRepeatSubmit.lockTime();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String key = getKey(request);
        try {
            Boolean isLock = redisUtil.tryLock(key);
            // 如果缓存中有这个url视为重复提交
            if (!isLock) {
                // 执行前 添加锁
                redisUtil.addLock(key, 0, lockSeconds);
                Object o = pjp.proceed();
                return o;
            } else {
                log.info("重复提交");
                return ResponseData.oferror("请勿重复提交");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("验证重复提交时出现未知异常!");
            throw new RuntimeException(e);
        } finally {
            // 执行后 删除锁
            redisUtil.releaseLock(key);
        }
    }

    private String getKey(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String key = token + ":" + request.getServletPath();
        return key;
    }

}
