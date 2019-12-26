package com.kailaisi.handwritemvc.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 22:13
 */
public class KProxyChain {
    private Class<?> targetClass;
    private Method targetMethod;
    private Object[] methodParams;

    public KProxyChain(Class<?> targetClass, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<KProxy> proxyList) {

    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Object[] getMethodParams() {

        return methodParams;
    }

    public void setMethodParams(Object[] methodParams) {
        this.methodParams = methodParams;
    }

    /**
     * 递归调用
     * @return
     */
    public Object doProxyChain() {
        return null;
    }
}
