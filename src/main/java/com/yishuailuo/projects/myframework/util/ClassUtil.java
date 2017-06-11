package com.yishuailuo.projects.myframework.util;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by luoyishuai on 17/6/11.
 */
@Slf4j
public final class ClassUtil {

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String classname, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(classname, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return cls;
    }


    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = Sets.newLinkedHashSet();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String packagePath = StringUtils.replace(url.getPath(), "%20", " ");
                        addClass(classSet, packagePath, packageName);
                    } else if ("jar".equals(protocol)) {
                        JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
                        if (jarUrlConnection != null) {
                            JarFile jarFile = jarUrlConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (StringUtils.endsWith(jarEntryName, ".class")) {
                                        String className = StringUtils.replace(StringUtils.substring(jarEntryName, 0,
                                                StringUtils.lastIndexOf(jarEntryName, ".")), "/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("get class set failure", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {

        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {

        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && StringUtils.endsWith(file.getName(), ".class")) || file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = StringUtils.substring(fileName, 0, StringUtils.lastIndexOf(fileName, "."));
                if (StringUtils.isNotBlank(packageName)) {
                    className = packageName + "." + className;
                }

                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotBlank(packagePath)) {
                    subPackagePath = packagePath + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }

                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

}
