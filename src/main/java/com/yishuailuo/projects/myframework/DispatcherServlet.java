package com.yishuailuo.projects.myframework;

import com.google.common.collect.Maps;
import com.yishuailuo.projects.myframework.bean.Data;
import com.yishuailuo.projects.myframework.bean.Handler;
import com.yishuailuo.projects.myframework.bean.Param;
import com.yishuailuo.projects.myframework.bean.View;
import com.yishuailuo.projects.myframework.helper.BeanHelper;
import com.yishuailuo.projects.myframework.helper.ConfigHelper;
import com.yishuailuo.projects.myframework.helper.ControllerHelper;
import com.yishuailuo.projects.myframework.util.CodecUtil;
import com.yishuailuo.projects.myframework.util.JsonUtil;
import com.yishuailuo.projects.myframework.util.ReflectionUtil;
import com.yishuailuo.projects.myframework.util.StreamUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by luoyishuai on 17/6/11.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        HelperLoader.init();
        ServletContext servletContext = servletConfig.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = StringUtils.lowerCase(req.getMethod());
        String requestPath = req.getPathInfo();

        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            Map<String, Object> paramMap = Maps.newHashMap();
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
            String body = CodecUtil.decodeUrl(StreamUtil.getString(req.getInputStream()));

            if (StringUtils.isNotBlank(body)) {
                String[] params = StringUtils.split(body, "&");
                if (ArrayUtils.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] array = StringUtils.split(param, "=");
                        if (ArrayUtils.isNotEmpty(array)) {
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }

            Param param = Param.builder().paramMap(paramMap).build();

            Method actionMethod = handler.getActionMethods();
            Object result = ReflectionUtil.invodeMethod(controllerBean, actionMethod, param);
            if (result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if (StringUtils.isNotBlank(path)) {
                    if (StringUtils.startsWith(path, "/")) {
                        resp.sendRedirect(req.getContextPath() + path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                    }
                } else {
                    Data data = (Data) result;
                    Object model = data.getModel();
                    if (model != null) {
                        resp.setContentType("application/json");
                        resp.setCharacterEncoding("UTF-8");
                        PrintWriter printWriter = resp.getWriter();
                        String json = JsonUtil.toJson(model);
                        printWriter.write(json);
                        printWriter.flush();
                        printWriter.close();
                    }
                }
            }

        }
    }
}
