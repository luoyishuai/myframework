package com.yishuailuo.projects.myframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by luoyishuai on 17/6/11.
 */
@Slf4j
public final class StreamUtil {

    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error("get String failure", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
