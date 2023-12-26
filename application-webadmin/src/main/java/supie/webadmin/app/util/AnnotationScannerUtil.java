package supie.webadmin.app.util;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 注解扫描工具类，扫描指定包下所有的类，并输出类名和注解值
 */
public class AnnotationScannerUtil {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 扫描的包路径，根据实际项目设置
        String basePackage = "supie";

        List<Class<?>> classes = getClasses(basePackage);

        // 记录上一个类所在的包
        String lastPackageName = "";
        int totalSum = 0;
        // 遍历并输出类的全路径及注解值
        for (Class<?> clazz : classes) {
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
//                System.out.println("Class: " + clazz.getName() + ", TableName: " + tableNameAnnotation.value());
                if (!clazz.getPackage().getName().equals(lastPackageName)) {
//                    System.out.println("\n\n===========================================》 " + clazz.getPackage().getName() + " 《===========================================");
                    System.out.println();
                }
                lastPackageName = clazz.getPackage().getName();
                System.out.print("\"" + tableNameAnnotation.value() + "\",");
                totalSum++;
            }
        }
        System.out.println("\ntotalSum: " + totalSum);
    }

    private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            classes.addAll(findClasses(new File(resource.getFile()), packageName));
        }

        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }

}
