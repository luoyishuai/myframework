package com.yishuailuo.projects.myframework.annotation;

import java.lang.annotation.*;

/**
 * Created by luoyishuai on 17/6/19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
