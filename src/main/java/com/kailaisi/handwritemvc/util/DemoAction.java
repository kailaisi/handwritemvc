package com.kailaisi.handwritemvc.util;

import com.kailaisi.handwritemvc.annotation.KAutowired;
import com.kailaisi.handwritemvc.annotation.KController;
import com.kailaisi.handwritemvc.annotation.KRequestMapping;
import com.kailaisi.handwritemvc.annotation.KRequestParam;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/25 22:07
 */
@KController
@KRequestMapping("/demo")
public class DemoAction {
    @KAutowired
    private IDemoService demoService;

    @KRequestMapping("/add.do")
    public String get(@KRequestParam String name){
        return demoService.query(name);
    }
}
