package studio.wetrack.base.page;

/**
 * Created by zhanghong on 16/9/19.
 */
public class PageableQueryParam extends Limit {
    Object searchParam;


    public void setSearchParam(Object searchParam){
        this.searchParam = searchParam;
    }

    public Object getSearchParam(){
        return this.searchParam;
    }
}
