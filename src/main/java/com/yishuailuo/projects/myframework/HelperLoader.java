package com.yishuailuo.projects.myframework;

import com.yishuailuo.projects.myframework.helper.BeanHelper;
import com.yishuailuo.projects.myframework.helper.ClassHelper;
import com.yishuailuo.projects.myframework.helper.ControllerHelper;
import com.yishuailuo.projects.myframework.helper.IocHelper;
import com.yishuailuo.projects.myframework.util.ClassUtil;

/**
 * Created by luoyishuai on 17/6/11.
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classes = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : classes) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
