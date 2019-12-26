package com.kailaisi.handwritemvc.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/25 21:50
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KAutowired {
    String value() default "";
}
