package com.kailaisi.handwritemvc.aop;

import java.lang.reflect.Method;

/**
 * 描述：切面抽象类
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 22:13
 */
public abstract class KAspectProxy implements KProxy {
    @Override
    public Object doProxy(KProxyChain chain){
        Object result=null;
        Class<?> cls = chain.getTargetClass();
        Method method = chain.getTargetMethod();
        Object[] params = chain.getMethodParams();
        begin();
        try {
            if(intercept(method,params)){
                before(method,params);
                result=chain.doProxyChain();
                after(method,params);
            }else {
                result=chain.doProxyChain();
            }
        }catch (Exception e){
            error(method,params,e);
        }finally {
            end();
        }
        return result;
    }

    protected  void end(){}


    protected void error(Method method, Object[] params, Exception e) {

    }

    protected void after(Method method, Object[] params) {

    }

    protected void before(Method method, Object[] params) {

    }

    protected boolean intercept(Method method, Object[] params){
        return true;
    }

    protected void begin() {

    }
}
