package studio.wetrack.docgen;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import studio.wetrack.base.utils.ClassSanner;
import studio.wetrack.docgen.annot.ApiDoc;
import studio.wetrack.docgen.markdown.MarkdownFile;
import studio.wetrack.web.filter.AjaxResponseWrapper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Created by zhanghong on 17/3/9.
 */
public class Builder {

    String packageName;
    String outputPath;
    public Builder(String packageName, String outputPath){
        this.packageName = packageName;
        this.outputPath = outputPath;
    }

    public boolean build() throws DocGenException, IOException {
        List<Class> clses = ClassSanner.scanClassesOfAnnotationInPackage(Controller.class, packageName);

        for (Class cls : clses) {
            apiDocForClass(cls);
        }
        //class本身有@RequestMapping的


        //方法中有requestMapping的

        return true;
    }

    /**
     * 处理一个类，生成整个类内的文档
     * @param cls
     * @throws DocGenException
     * @throws IOException
     */
    private void apiDocForClass(Class cls) throws DocGenException, IOException {
        MarkdownFile doc = new MarkdownFile(outputPath, cls.getSimpleName());

        String apiBasePath = "";

        if(cls.isAnnotationPresent(RequestMapping.class)){
            RequestMapping annot = (RequestMapping)cls.getDeclaredAnnotation(RequestMapping.class);
            String[] basePaths = extractPathFromRequestMappingAnnot(annot);
            if(basePaths != null && basePaths.length > 0){
                apiBasePath = basePaths[0];
            }
        }

        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            //这是一个接口方法吗
            if(method.isAnnotationPresent(ApiDoc.class)){
                if(method.isAnnotationPresent(RequestMapping.class)){
                    HttpApi api = apiFromRequestMapping(apiBasePath, method, method.getDeclaredAnnotation(RequestMapping.class));
                    doc.write(api.toMarkDownString());
                }else{
                    //todo 暂不支持
                    throw new DocGenException("接口目前只支持通过RequestMapping注解生成文档");
                }
            }
        }

        doc.generate();
    }

    /**
     * 处理一个接口，生成一个接口文档节点
     * @param basePath
     * @param method
     * @param annot
     * @return
     */
    private HttpApi apiFromRequestMapping(String basePath, Method method, RequestMapping annot) {
        ApiDoc apiDocAnnot = method.getDeclaredAnnotation(ApiDoc.class);

        //1 接口路径
        String[] paths = extractPathFromRequestMappingAnnot(annot);
        for (int i = 0; i < paths.length; i++) {
            paths[i] = basePath + paths[i];
        }

        //2 接口请求方法
        RequestMethod[] requestMethods = annot.method();
        if(requestMethods == null || requestMethods.length == 0){
            //如果没有明确定义请求方法的，根据参数中是否存在requestBody来判断是不是限制为post方法
            if(isMethodAcceptRequestBody(method)){
                requestMethods = new RequestMethod[]{RequestMethod.POST};
            }else{
                requestMethods = new RequestMethod[]{RequestMethod.GET};
            }
        }

        boolean returnTypeWrapper = method.getDeclaringClass().isAnnotationPresent(AjaxResponseWrapper.class) || method.isAnnotationPresent(AjaxResponseWrapper.class);


        //3. 接口返回类型,注意嵌套的子类型

        HttpApi api = new HttpApi(apiDocAnnot.name().length() > 0 ? apiDocAnnot.name() : method.getName(), apiDocAnnot.descript(), requestMethods, paths, method.getGenericReturnType(), returnTypeWrapper);

        //4. 接口参数
        for (Parameter parameter : method.getParameters()) {
            Class dataType = parameter.getType();
            String name = parameter.getName();
            boolean required = true;
            HttpApi.ParamType paramType = HttpApi.ParamType.query;
            if(parameter.isAnnotationPresent(PathVariable.class)){
                PathVariable pv = parameter.getAnnotation(PathVariable.class);
                if(!StringUtils.isEmpty(pv.name())){
                    name = pv.name();
                }
                required = pv.required();
                paramType = HttpApi.ParamType.path;
            }else if(parameter.isAnnotationPresent(RequestBody.class)){
                //一定要是post
                RequestBody rb = parameter.getAnnotation(RequestBody.class);
                required = rb.required();
                paramType = HttpApi.ParamType.body;
            }else{
                if(parameter.isAnnotationPresent(RequestParam.class)){
                    RequestParam rp = parameter.getAnnotation(RequestParam.class);
                    if(rp.name().length() > 0){
                        name = rp.name();
                    }
                    required = rp.required();
                }
                paramType = HttpApi.ParamType.query;
            }
            HttpApi.HttpApiParam aParam = new HttpApi.HttpApiParam(name, required, paramType, dataType, null);
            api.addParam(aParam);
        }


        return api;

    }

    private boolean isMethodAcceptRequestBody(Method method){
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if(annotation.annotationType().equals(RequestBody.class))
                    return true;
            }
        }
        return false;
    }

    private String[] extractPathFromRequestMappingAnnot(RequestMapping annot){
        String[] paths = annot.path();
        String[] values = annot.value();
        if(paths.length > 0){
            return paths;
        }else if(values.length > 0){
            return values;
        }
        return null;
    }
}
