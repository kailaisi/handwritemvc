package com.kailaisi.handwritemvc.aop;

/**
 * 描述：代理接口
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 22:11
 */
public interface KProxy {
    /**
     * 执行链式代理
     * @param chain  可以将多个代理通过链式串行起来，一个个去执行
     * @return
     * @throws Throwable
     */
    Object doProxy(KProxyChain chain) throws  Throwable;
}
