package studio.wetrack.accountService.domain;

/**
 * Created by zhanghong on 16/7/18.
 */

public class LoginOut {
    String token;
    String id;
    Type type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}