package com.kailaisi.handwritemvc.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 描述：代理工厂，通过该工程，可以创建代理对象，然后每次执行代理方法的时候，都调用intercept()方法
 * ，从而创建ProxyChain对象，并调用doProxyChain()方法。然后递归调用。。最终执行目标方法
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 22:22
 */
public class KProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<KProxy> proxyList){
      return (T)  Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return new KProxyChain(targetClass,method,methodProxy,objects,proxyList);
            }
        });
    }
}
