package com.github.cookzhang.ais.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: zhangyi
 * Date: 5/5/14
 * Time: 9:18
 * Description:
 */
public class Scanner {
    public static List<String> getClassName(String packageName) {
        List<String> classNames = new ArrayList<String>();
        if (packageName == null) {
            return classNames;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            String resourceName = packageName.replaceAll("\\.", "/");
            URL url = loader.getResource(resourceName);
            File urlFile = new File(url.toURI());
            File[] files = urlFile.listFiles();
            if (files == null || files.length == 0) {
                return classNames;
            }

            for (File f : files)
                getClassName(packageName, f, classNames);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    private static void getClassName(String packageName, File packageFile, List<String> list) {
        if (packageFile.isFile()) {
            list.add(packageName + "." + packageFile.getName().replace(".class", ""));
        } else {
            File[] files = packageFile.listFiles();
            String tmPackageName = packageName + "." + packageFile.getName();
            if (files == null) {
                return;
            }

            for (File f : files) {
                getClassName(tmPackageName, f, list);
            }
        }
    }
}
