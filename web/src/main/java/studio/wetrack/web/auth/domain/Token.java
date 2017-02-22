package studio.wetrack.web.auth.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhanghong on 15/11/18.
 * 业务无关的通用框架模型
 * 基于token的权限验证的token模型
 */
public class Token implements Serializable{

    private long created = System.currentTimeMillis();
    private final String token;
    private User user;
    private boolean loggedout; //token已经退出

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public boolean isExpired(){
        return !user.isNeverExpired() && ((System.currentTimeMillis() - created)/1000) > user.getLoginLifeTime();
    }

    public  boolean isLoggedout(){
        return loggedout;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setLoggedout(boolean loggedout) {
        this.loggedout = loggedout;
    }

    @Override
    public String toString() {
        return  "token='" + token + '\'' +
                ", User : " + user +
                ", created=" + new Date(created) +
                '}';
    }
}
