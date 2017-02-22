package studio.wetrack.base.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by zhanghong on 16/12/23.
 */
public class ClassSanner {

    public static List<Class> scanClassesOfTypeInPackages(Class type, String... packageNames){
        List<Class> clses = new ArrayList<>();
        for(String packageName : packageNames){
            clses.addAll(scanClassesOfTypeInPackage(type, packageName));
        }

        //去重
        List<Class> filtered = new ArrayList<>();
        for(Class cls : clses){
            if(!filtered.contains(cls)){
                filtered.add(cls);
            }
        }

        return filtered;
    }

    public static List<Class> scanClassesOfTypeInPackage(Class type, String packageName){
        Class[] clses = new Class[0];
        try {
            clses = getClasses(packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Class> filtered = new ArrayList<>();
        for(Class cls : clses){
            if(type.isAssignableFrom(cls)){
                filtered.add(cls);
            }
        }

        return filtered;
    }

    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
