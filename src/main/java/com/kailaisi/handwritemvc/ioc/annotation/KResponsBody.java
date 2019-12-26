package com.kailaisi.handwritemvc.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/25 21:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface KResponsBody {
    String value() default "";
}
