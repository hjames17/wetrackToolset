package studio.wetrack.accountService.domain;

/**
 * Created by zhanghong on 16/7/18.
 * smart login 表单根据用户输入的内容形式智能匹配到LoginForm来进行登录
 * 可以识别手机号码登录，邮箱登录以及用户名登录
 */
public class SmartLoginForm {
    String account;
    String password;
    Type type;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
