package com.kailaisi.handwritemvc.ioc

import org.slf4j.LoggerFactory
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

/**
 *描述：
 *<p/>作者：kailaisii
 *<br/>创建时间：2020/1/7 23:39
 */
class MyInitDataListener :ServletContextListener {

    override fun contextInitialized(sce: ServletContextEvent?) {
        var loader = sce!!.servletContext.classLoader
     //   println("MyInitDataListener.contextInitialized",loader.toString())
 /*       PropsUtil.loadProps()
        //加载xml的配置文件
        KDispatcherServlet.doLoadConfig(config.getInitParameter("contextConfigLocation"))
        //扫描相关类
        KDispatcherServlet.doScan(KDispatcherServlet.p.getProperty("scanPackage"))
        //初始化所有的类实例，并保存到IOC容器中
        KDispatcherServlet.doInstance()
        //依赖注入
        KDispatcherServlet.doAutowired()
        //构造HandlerMapping
        KDispatcherServlet.initHandlerMapping()*/
    }
}