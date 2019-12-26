package com.kailaisi.handwritemvc.ioc.util;

import com.kailaisi.handwritemvc.ioc.annotation.KResponsBody;
import com.kailaisi.handwritemvc.utils.ClassUtil;
import com.kailaisi.handwritemvc.utils.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/25 21:57
 */
public class KDispatcherServlet extends HttpServlet {
    public void init(ServletConfig config) {
        new IOCHelper(config);
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
     *
     * @param req
     * @param resp
     * @throws Exception
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        boolean json = false;
        Map<String, Method> mapping = IOCHelper.getHandlerMapping();
        if (mapping.isEmpty()) {
            throw new IllegalArgumentException("地址错误");
        }
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String url = uri.replace(contextPath, "").replaceAll("//", "/");
        if (!mapping.containsKey(url)) {
            resp.getWriter().write("404 ,url not found!");
        }
        Map<String, String[]> params = req.getParameterMap();
        Method method = mapping.get(url);
        if (method.isAnnotationPresent(KResponsBody.class) || method.getDeclaringClass().isAnnotationPresent(KResponsBody.class)) {
            json = true;
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
            String bean = StringUtil.lowerFirstCase(method.getDeclaringClass().getSimpleName());
            Object o = IOCHelper.get(bean);
            Object o1 = method.invoke(o, objects);
            if (json) {
                resp.getWriter().write(o1.toString());
            }
            resp.getWriter().write(o1.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
