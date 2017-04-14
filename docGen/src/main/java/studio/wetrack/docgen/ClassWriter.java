package studio.wetrack.docgen;

import java.lang.reflect.Field;

/**
 * Created by zhanghong on 17/3/10.
 * 把一个类以及他的数据字段按照java类定义格式输出
 */
public class ClassWriter {

    Class cls;
    StringBuilder sb;

    public enum Format {
        table,
        cls,
        ;
    }

    public ClassWriter(Class cls){
        this.cls = cls;
        this.sb = new StringBuilder();
    }

    public String toMarkDownString(){

        sb.append("``\n")
                .append(cls.toGenericString()).append('{').append('\n');
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            sb.append("\t")
                    .append(field.getType().getSimpleName()).append(' ')
                    .append(field.getName())
                    .append(';')
                    .append('\n');
        }
        sb.append('}').append("``\n");

        return sb.toString();

    }
}
