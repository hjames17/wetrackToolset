package studio.wetrack.docgen;

import studio.wetrack.docgen.markdown.MarkdownTable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhanghong on 17/3/10.
 * 把一个类以及他的数据字段按照java类定义格式输出
 */
public class TypeWriter {

    Type type;
    Class cls;
    StringBuilder sb;

    public enum Format {
        table,
        cls,
        ;
    }

    public TypeWriter(Type cls) throws ClassNotFoundException {
        this.type = cls;
        this.cls = getGenericType();
        this.sb = new StringBuilder();
    }

    public String toMarkDownString(Format format){
        if(format == Format.table){
            MarkdownTable table = new MarkdownTable("名称", "类型", "是否必须", "参数说明");
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                table.addRow(field.getName(), field.getType().getSimpleName());
            }
            return table.toString();
        }else {
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

    public Class getGenericType() throws ClassNotFoundException {
        if(type instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType)type;
            return Class.forName(pt.getActualTypeArguments()[0].getTypeName());

        }else{
            return Class.forName(type.getTypeName());
        }
    }
}
