package com.kailaisi.handwritemvc.aop;

import com.kailaisi.handwritemvc.aop.annotation.KAspect;
import com.kailaisi.handwritemvc.utils.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/27 0:07
 */
@KAspect(pkg = "com.kailaisi.handwritemvc.mvc",cls = "DemoAction")
public class EfficientAspect extends KAspectProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(EfficientAspect.class);
    @Override
    protected boolean intercept(Method method, Object[] params) {
        return true;
    }

    @Override
    protected void before(Method method, Object[] params) {
        super.before(method, params);
        LOGGER.info("before 增强~~");
    }

    @Override
    protected void after(Method method, Object[] params) {
        super.after(method, params);
        LOGGER.info("after 增强~~");
    }
}
