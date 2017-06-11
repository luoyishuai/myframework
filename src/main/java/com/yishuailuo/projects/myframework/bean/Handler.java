package com.yishuailuo.projects.myframework.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * Created by luoyishuai on 17/6/11.
 */
@Getter
@AllArgsConstructor
@Builder
public class Handler {

    private Class<?> controllerClass;

    private Method actionMethods;


}
