package com.yishuailuo.projects.myframework.bean;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * Created by luoyishuai on 17/6/11.
 */
@Getter
public class View {

    private String path;

    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = Maps.newHashMap();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }
}
