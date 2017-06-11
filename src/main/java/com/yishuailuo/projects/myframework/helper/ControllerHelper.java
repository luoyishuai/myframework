package com.yishuailuo.projects.myframework.helper;

import com.google.common.collect.Maps;
import com.yishuailuo.projects.myframework.annotation.Action;
import com.yishuailuo.projects.myframework.bean.Handler;
import com.yishuailuo.projects.myframework.bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created by luoyishuai on 17/6/11.
 */
public final class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = Maps.newHashMap();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = Request.builder()
                                            .requestMethod(requestMethod)
                                            .requestPath(requestPath)
                                            .build();
                                    Handler handler = Handler.builder()
                                            .controllerClass(controllerClass)
                                            .actionMethods(method)
                                            .build();
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = Request.builder()
                .requestMethod(requestMethod)
                .requestPath(requestPath)
                .build();
        return ACTION_MAP.get(request);
    }
}
