package com.kailaisi.handwritemvc.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 22:05
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface KAspect {
    /**
     * 包名
     *
     * @return
     */
    String pkg() default "";

    /**
     * 类名
     *
     * @return
     */
    String cls() default "";
}
