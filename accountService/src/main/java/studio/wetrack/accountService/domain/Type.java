package studio.wetrack.accountService.domain;

import java.io.Serializable;

/**
 * Created by zhanghong on 16/7/18.
 */
public class Type implements Serializable{

    String name;
    String[] rolesStringArray;

    public String getName(){
        return name;
    }
    public void setName(String name){

        this.name = name;
    }

    public void setRolesStringArray(String[] rolesStringArray){
        this.rolesStringArray = rolesStringArray;
    }

    public String[] getRolesStringArray(){

        return rolesStringArray;
    }
}
