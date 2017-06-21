package com.yishuailuo.projects.myframework.proxy;

import com.yishuailuo.projects.myframework.annotation.Aspect;
import com.yishuailuo.projects.myframework.annotation.Controller;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Created by luoyishuai on 17/6/20.
 */
@Slf4j
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        log.info("---begin---");
        log.info(String.format("class: %s", cls.getName()));
        log.info(String.format("method: %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {

        log.info(String.format("time: %dms", System.currentTimeMillis() - begin));
        log.info("---after---");
    }
}
