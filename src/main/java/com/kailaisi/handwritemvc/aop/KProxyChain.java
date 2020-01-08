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
    private MethodProxy methodProxy;
    private Method targetMethod;
    private Object[] methodParams;
    private List<KProxy> proxyList;
    private static Integer proxyExecuteIndex = 0;

    public KProxyChain(Class<?> targetClass, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<KProxy> proxyList) {
        this.targetClass = targetClass;
        this.methodProxy = methodProxy;
        this.targetMethod = targetMethod;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }


    public Object[] getMethodParams() {
        return methodParams;
    }

    /**
     * 递归调用
     *
     * @return
     */
    public Object doProxyChain() {
        Object result = null;
        if (proxyExecuteIndex < proxyList.size()) {
            result = proxyList.get(proxyExecuteIndex++).doProxy(this);
        } else {
            try {
                result = methodProxy.invoke(targetClass, methodParams);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return result;
    }
}
