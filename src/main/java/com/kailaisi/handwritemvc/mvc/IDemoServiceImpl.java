package com.kailaisi.handwritemvc.mvc;

import com.kailaisi.handwritemvc.ioc.annotation.KService;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/25 22:10
 */
@KService
public class IDemoServiceImpl implements IDemoService {
    public String query(String name) {
        return name+",hai";
    }
}
