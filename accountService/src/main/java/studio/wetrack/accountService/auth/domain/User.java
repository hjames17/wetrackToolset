package studio.wetrack.accountService.auth.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zhanghong on 15/11/18.
 * 业务无关的通用框架模型
 * 权限管理的通用用户模型
 * 业务相关的用户数据模型通过映射创建出该对象
 */
public class User implements UserDetails {

    public static final int NEVER_EXPIRED = -1;
    public static final int LOGIN_COUNT_UNLIMIT = -1;

    String id;
    String password;
    int loginLifeTime; //登录有效期，单位为秒
    int maxLoginCount = 1; //最大登录数，默认为1

    /**
     * 角色定义，这里不做预定义，由业务自行定义
     * 但是有一个full角色，默认匹配所有的权限,
     * 可以通过setFullRoleName重新为其指定一个名称
     */
    Collection<GrantedAuthority> roles;
    public static String ROLE_FULL = "full";

    public static void setFullRoleName(String fullRole){
        ROLE_FULL = fullRole;
    }


    /**
     * User构造函数
     * @param id id
     * @param password password
     * @param loginLifeTime 过期时间
     * @param roles 角色名称的集合
     */
    public User(String id, String password, int loginLifeTime, String... roles){
        this.id = id;
        this.password = password;
        this.loginLifeTime = loginLifeTime;
        this.roles = new ArrayList<GrantedAuthority>();
        if(roles != null) {
            for (String r : roles) {
                this.roles.add(new SimpleGrantedAuthority(r));
            }
        }

    }



    /**
     * User构造函数
     * @param id id
     * @param password password
     * @param loginLifeTime 过期时间
     * @param roles 角色名称的集合
     */
    public User(String id, String password, int loginLifeTime, int maxLoginCount, String... roles){
        this(id, password, loginLifeTime, roles);
        this.maxLoginCount = maxLoginCount;
    }

    public String getId(){
        return id;
    }

    public boolean hasRole(String role){

        if(role.equals(ROLE_FULL)){
            return true;
        }

        for(GrantedAuthority r : roles){
            if(r.getAuthority().equals(role)){
                return true;
            }
        }
        return false;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return id;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public int getLoginLifeTime() {
        return loginLifeTime;
    }

    public void setLoginLifeTime(int loginLifeTime) {
        this.loginLifeTime = loginLifeTime;
    }

    public boolean isNeverExpired(){
        return loginLifeTime == NEVER_EXPIRED;
    }

    public int getMaxLoginCount() {
        return maxLoginCount;
    }

    public void setMaxLoginCount(int maxLoginCount) {
        this.maxLoginCount = maxLoginCount;
    }

    public boolean maxLoginLimitReached(int count){
        if(LOGIN_COUNT_UNLIMIT == maxLoginCount){
            return false;
        }
        return count >= maxLoginCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", loginLifeTime=" + loginLifeTime +
                ", maxLoginCount=" + maxLoginCount +
                ", roles=" + roles +
                '}';
    }

    public static void main(String[] args){
        User u = new User("1", "123", -1, 2, ROLE_FULL);
        System.out.println(u.toString());
    }
}
