package studio.wetrack.base.page;

import org.springframework.data.domain.Pageable;

/**
 * Created by zhanghong on 16/9/18.
 */
public class PageInstaller {

    static int defaultPageSize = 20;

    public static <T extends Object> PageableQueryParam installPageable(T searchParam, Pageable pageable){
        PageableQueryParam pageableQueryParam = new PageableQueryParam();
        pageableQueryParam.setSearchParam(searchParam);
        int page = 0;
        int pageSize = defaultPageSize;
        if(pageable != null){
            page = pageable.getPageNumber();
            pageSize = pageable.getPageSize();
        }
        if(page < 0){
            throw new PageSearchInstallException("page search install error, page number could not be negative but get " + page);
        }
        if(pageSize <= 0){
            throw new PageSearchInstallException("page search install error, page size must be larger than 0 but get " + pageSize);
        }
        pageableQueryParam.start = page * pageSize;
        pageableQueryParam.size = pageSize;
        return pageableQueryParam;
    }

    public static void setDefaultPageSize(int size){
        PageInstaller.defaultPageSize = size;
    }

    public static int getDefaultPageSize(){
        return PageInstaller.defaultPageSize;
    }


}
