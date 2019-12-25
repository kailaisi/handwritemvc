package com.kailaisi.handwritemvc.util;

import com.kailaisi.handwritemvc.annotation.*;
import jdk.nashorn.internal.parser.JSONParser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/25 21:57
 */
public class KDispatcherServlet extends HttpServlet {
    private Properties p = new Properties();
    private LinkedList<String> clazzName = new LinkedList<String>();
    private Map<String, Object> ioc = new HashMap<String, Object>();
    private Map<String, Method> handlerMapping = new HashMap<String, Method>();


    public void init(ServletConfig config) {
        //加载xml的配置文件
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500");
        }
    }

    /**
     * 拦截
     * @param req
     * @param resp
     * @throws Exception
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        boolean json=false;
        if (this.handlerMapping.isEmpty()) {
            throw new IllegalArgumentException("地址错误");
        }
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String url = uri.replace(contextPath, "").replaceAll("//", "/");
        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 ,url not found!");
        }
        Map<String, String[]> params = req.getParameterMap();
        Method method = handlerMapping.get(url);
        if(method.isAnnotationPresent(KResponsBody.class)|| method.getDeclaringClass().isAnnotationPresent(KResponsBody.class)){
            json=true;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] objects = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type == HttpServletRequest.class) {
                objects[i] = req;
            } else if (type == HttpServletResponse.class) {
                objects[i] = resp;
            } else if (type == String.class) {
                for (Map.Entry<String, String[]> entry : params.entrySet()) {
                    String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "")
                            .replaceAll("\\s", "");
                    objects[i] = value;
                }
            }
        }
        try {
            String bean = lowerFirstCase(method.getDeclaringClass().getSimpleName());
            Object o = ioc.get(bean);
            Object o1 = method.invoke(o, objects);
            if(json){
                resp.getWriter().write(o1.toString());
            }
            resp.getWriter().write(o1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initHandlerMapping() {
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

    private void doAutowired() {
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

    private void doInstance() {
        if (clazzName.size() == 0) {
            return;
        }
        try {
            for (String s : clazzName) {
                Class<?> aClass = Class.forName(s);
                if (aClass.isAnnotationPresent(KController.class)) {
                    String beanName = lowerFirstCase(aClass.getSimpleName());
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

    private String lowerFirstCase(String str) {
        char[] array = str.toCharArray();
        array[0] += 32;
        return String.valueOf(array);
    }


    private void doScan(String scanPackage) {
        URL url = getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else {
                clazzName.add(scanPackage + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doLoadConfig(String location) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(location);
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


}
