package com.yishuailuo.projects.myframework.proxy;

/**
 * Created by luoyishuai on 17/6/19.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
