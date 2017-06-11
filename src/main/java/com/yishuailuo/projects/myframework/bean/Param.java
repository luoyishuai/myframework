package com.yishuailuo.projects.myframework.bean;

import com.yishuailuo.projects.myframework.util.CastUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Created by luoyishuai on 17/6/11.
 */
@Builder
public class Param {

    @Getter
    private Map<String, Object> paramMap;

    public long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }
}
