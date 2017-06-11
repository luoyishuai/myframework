package com.yishuailuo.projects.myframework.bean;

import lombok.Builder;
import lombok.Data;

/**
 * Created by luoyishuai on 17/6/11.
 */
@Data
@Builder
public class Request {

    private String requestMethod;

    private String requestPath;

}
