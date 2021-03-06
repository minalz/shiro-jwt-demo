package cn.minalz.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
    /**
     * 业务名称
     *
     * @return
     */
    String businessName() default "操作日志";

    /**
     * 业务类型
     *
     * @return
     */
    String businessType() default "system";
}
