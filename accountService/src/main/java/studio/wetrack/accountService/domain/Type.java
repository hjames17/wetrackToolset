package studio.wetrack.accountService.domain;

/**
 * Created by zhanghong on 16/7/18.
 */
public class Type {

    String name;
    String[] rolesStringArray;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String[] getRolesStringArray(){
        return rolesStringArray;
    }
}
