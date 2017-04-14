package studio.wetrack.docgen;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.wetrack.base.utils.TypeUtil;
import studio.wetrack.docgen.markdown.MarkdownTable;
import studio.wetrack.web.result.AjaxResult;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhanghong on 17/3/10.
 */
public class HttpApi {

    String name;
    String descript;

    public long test(){
        return 1l;
    }
    public List<String> test1(){
        return Arrays.asList("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public RequestMethod[] getMethods() {
        return methods;
    }

    public void setMethods(RequestMethod[] methods) {
        this.methods = methods;
    }

    boolean returnTypeWrapped;
    RequestMethod[] methods;
    String[] paths;
    List<HttpApiParam> params;
    Type returnType;

    public HttpApi(String name, String descript, RequestMethod[] methods, String[] paths, Type returnType, boolean returnTypeWrapped){

        this.name = name;
        this.descript = descript;
        this.methods = methods;
        this.paths = paths;
        this.returnType = returnType;
        this.returnTypeWrapped = returnTypeWrapped;
        this.params = new ArrayList<>();
    }

    public void addParam(HttpApiParam apiParam){
        this.params.add(apiParam);
    }

    public static class HttpApiParam{
        String name;
        boolean required;
        ParamType type;
        Class dataType;
        String comment;

        public HttpApiParam(String name, boolean required, ParamType type, Class dataType, String comment){
            this.name = name;
            this.required = required;
            this.type = type;
            this.dataType = dataType;
            this.comment = comment;
        }

    }

    public enum ParamType{
        path,
        query,
        body,
        ;

    }

    public boolean isPost(){
        for (RequestMethod method : methods) {
            if(method.equals(RequestMethod.POST))
                return true;
        }
        return false;
    }

    public String toMarkDownString() {
        StringBuilder sb = new StringBuilder();

        //接口名称
        sb.append("##").append(name).append('\n');
        //接口描述
        if(!StringUtils.isEmpty(descript)) {
            sb.append(">[info]").append(descript).append('\n');
        }

        //接口url
        sb.append("###url ").
                append(String.join("/", Arrays.asList(methods).stream().map(method -> method.name()).collect(Collectors.toList())))
                .append(':')
                .append(String.join(",", paths))
                .append('\n');

        //接口参数
        if(params.size() > 0){
            sb.append("###参数").append('\n');

            HttpApiParam postBody = null;

            //url上的参数
            MarkdownTable markdownTable = new MarkdownTable("参数名", "参数类型", "是否必须", "参数说明");
            for (HttpApiParam param : params) {
                if(param.type.equals(ParamType.body)){
                    postBody = param;
                    continue;
                }
                markdownTable.addRow(param.name, param.dataType.getSimpleName(), param.required + "", param.comment);
            }
            //post的数据参数
            if(postBody != null){
                sb.append("###post参数").append('\n');
                try {
                    TypeWriter cw = new TypeWriter(postBody.dataType);
                    sb.append(cw.toMarkDownString(TypeWriter.Format.table));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            sb.append(markdownTable.toString());
        }

        //返回值
        if(returnType != null && !TypeUtil.isVoid(returnType)){
            sb.append("###返回值").append('\n');
            if(returnTypeWrapped){
                TypeWriter cw = null;
                try {
                    cw = new TypeWriter(AjaxResult.class);
                    sb.append(cw.toMarkDownString(TypeWriter.Format.cls));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(!TypeUtil.isSystemType(returnType)) {

                sb.append("\ndata数据").append("(").append(returnType.getTypeName()).append(")").append("\n");
//                Class dataType = null;
//                if(returnType instanceof ParameterizedType){
//                    ParameterizedType pt = (ParameterizedType)returnType;
//                    try {
//                        dataType = Class.forName(pt.getActualTypeArguments()[0].getTypeName());
//                    } catch (ClassNotFoundException e) {
//                        sb.append("data type " + pt.getActualTypeArguments()[0].getTypeName() + " could not be found");
//                    }
//                }else{
//                    dataType = (Class)returnType;
//                }

//                if(dataType != null) {
                    TypeWriter rtcw = null;
                    try {
                        rtcw = new TypeWriter(returnType);
                        sb.append(rtcw.toMarkDownString(TypeWriter.Format.cls));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
//                }
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchMethodException {
//        List<String> s = new ArrayList<String>();
//        Map<String, String> l = new HashMap<>();
//        System.out.println(l.getClass() + " is a Map type " + TypeUtil.isMap(l.getClass()));
//
//        System.out.println((((ParameterizedType) s.getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[0]));
        Class HA = HttpApi.class;
        Method getName = HA.getMethod("test");
        Method setName = HA.getMethod("setName", String.class);
        Method tt1 = HA.getMethod("test1");
        Type t = getName.getGenericReturnType();
        Type t1 = tt1.getGenericReturnType();
        Type sn = setName.getGenericReturnType();
        System.out.print(t.getTypeName());
        System.out.print(t1.getTypeName());
        System.out.print(sn.getTypeName());
//
//        Class type = getName.getReturnType();
//        System.out.println(type + ", is primitive " + type.isPrimitive());
    }
}
