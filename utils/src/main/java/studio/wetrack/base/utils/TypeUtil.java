package studio.wetrack.base.utils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhanghong on 16/12/30.
 */
public class TypeUtil {



    public static boolean isVoid(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("void") || name.equals("java.lang.Void");
    }

    public static boolean isBoolean(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("boolean") || name.equals("java.lang.Boolean");
    }

    public static boolean isInt(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("int") || name.equals("java.lang.Integer");
    }

    public static boolean isLong(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("long") || name.equals("java.lang.Long");
    }

    public static boolean isDouble(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("double") || name.equals("java.lang.Double");
    }

    public static boolean isFloat(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("float") || name.equals("java.lang.Float");
    }

    public static boolean isString(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals("java.lang.String");
    }
    public static boolean isDate(Type t){
        if(t == null){
            return false;
        }
        String name = t.getTypeName();
        return name.equals(Date.class.getName());
    }

    public static boolean isCollection(Class c){
        return Collection.class.isAssignableFrom(c);
    }

    public static boolean isMap(Class c){
        return Map.class.isAssignableFrom(c);
    }

    public static boolean isPrimitive(Type t) {
        return isBoolean(t) || isInt(t) || isLong(t) || isDouble(t) || isFloat(t);
    }

    public static boolean isSystemType(Type t){
        return isPrimitive(t) || isString(t) || isDate(t);
    }
}
