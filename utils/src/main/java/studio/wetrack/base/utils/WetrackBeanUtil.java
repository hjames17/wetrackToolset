package studio.wetrack.base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhanghong on 16/9/27.
 */
public class WetrackBeanUtil {

    public static Object fillNullNumberFieldWithZero(Object bean, String... ignoreFields){
        List<String> ignores = null;
        if(ignoreFields != null){
            ignores = Arrays.asList(ignoreFields);
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            if(ignores != null && ignores.contains(field.getName())){
                //ignore it
                continue;
            }
            String typeName = field.getType().getTypeName();
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(bean);
            } catch (IllegalAccessException e) {

            }
            field.setAccessible(accessible);
            //if is not null value, ignore
            if(value != null){
                continue;
            }

            ////assign as 0 value
            if(typeName.equals(Integer.class.getTypeName()) || typeName.equals(Long.class.getTypeName())
                    || typeName.equals(Float.class.getTypeName()) || typeName.equals(Double.class.getTypeName())){
                field.setAccessible(true);
                try {
                    Class c = Class.forName(typeName);
                    Method m = c.getDeclaredMethod("valueOf", String.class);
                    field.set(bean, m.invoke(c, "0"));
                } catch (ClassNotFoundException e) {

                } catch (NoSuchMethodException e) {

                } catch (InvocationTargetException e) {

                } catch (IllegalAccessException e) {

                } finally {
                    field.setAccessible(accessible);
                }
                continue;
            }

        }
        return bean;
    }
}
