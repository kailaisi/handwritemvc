package com.kailaisi.handwritemvc.ioc.util;

import com.kailaisi.handwritemvc.ioc.annotation.KAutowired;
import com.kailaisi.handwritemvc.ioc.annotation.KController;
import com.kailaisi.handwritemvc.ioc.annotation.KRequestMapping;
import com.kailaisi.handwritemvc.ioc.annotation.KService;
import com.kailaisi.handwritemvc.utils.ClassUtil;
import com.kailaisi.handwritemvc.utils.StringUtil;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 23:06
 */
public final class IOCHelper {
    private static final Map<String, Object> ioc = new HashMap<String, Object>();
    private static final Properties p = new Properties();
    private static final LinkedList<String> clazzName = new LinkedList<String>();


    private static final Map<String, Method> handlerMapping = new HashMap<String, Method>();

    public static void init(ServletConfig config){
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //扫描相关类
        doScan(p.getProperty("scanPackage"));
        //初始化所有的类实例，并保存到IOC容器中
        doInstance();
        //依赖注入，将所有的autowired的属性注入到类中
        doAutowired();
        //构造HandlerMapping，将请求的url地址和对应的方法进行一一对应
        initHandlerMapping();
    }

    private static void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            String baseUrl = "";
            if (aClass.isAnnotationPresent(KRequestMapping.class)) {
                baseUrl = aClass.getAnnotation(KRequestMapping.class).value();
            }
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(KRequestMapping.class)) {
                    String url = method.getAnnotation(KRequestMapping.class).value();
                    String str = ("/" + baseUrl + url).replaceAll("//", "/");
                    handlerMapping.put(str, method);
                }
            }
        }
    }

    private static void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获取所有类的属性值
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(KAutowired.class)) {
                    KAutowired autowired = field.getAnnotation(KAutowired.class);
                    String beanName = autowired.value().trim();
                    if ("".equals(beanName)) {
                        beanName = field.getType().getName();
                    }
                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(), ioc.get(beanName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void doInstance() {
        if (clazzName.size() == 0) {
            return;
        }
        try {
            for (String s : clazzName) {
                Class<?> aClass = Class.forName(s);
                if (aClass.isAnnotationPresent(KController.class)) {
                    String beanName = StringUtil.lowerFirstCase(aClass.getSimpleName());
                    ioc.put(beanName, aClass.newInstance());
                } else if (aClass.isAnnotationPresent(KService.class)) {
                    KService kService = aClass.getAnnotation(KService.class);
                    if (!"".equals(kService.value().trim())) {
                        ioc.put(kService.value(), aClass.newInstance());
                        return;
                    } else {
                        Class<?>[] interfaces = aClass.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            ioc.put(anInterface.getName(), aClass.newInstance());
                        }
                    }
                } else {
                    continue;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void doScan(String scanPackage) {
        URL url = ClassUtil.getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else {
                clazzName.add(scanPackage + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private static void doLoadConfig(String location) {
        InputStream inputStream = null;
        try {
            inputStream = ClassUtil.getClassLoader().getResourceAsStream(location);
            p.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Object get(String bean) {
        return ioc.get(bean);
    }

    public static Map<String, Object> getIoc() {
        return ioc;
    }

    public static Map<String, Method> getHandlerMapping() {
        return handlerMapping;
    }

    public static void setBean(String targetClass, Object proxy) {
        ioc.put(targetClass, proxy);
    }
}
