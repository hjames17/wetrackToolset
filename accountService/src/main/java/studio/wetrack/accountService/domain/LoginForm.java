package studio.wetrack.accountService.domain;

/**
 * Created by zhanghong on 16/7/18.
 */
public class LoginForm{
    String uid;
    String email;
    String phone;
    String weixinId;
    String password;
    Type type;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}
}
