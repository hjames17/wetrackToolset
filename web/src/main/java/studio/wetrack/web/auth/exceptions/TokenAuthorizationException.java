package studio.wetrack.web.auth.exceptions;

/**
 * Created by zhanghong on 15/12/17.
 */
public class TokenAuthorizationException extends Exception {

    public TokenAuthorizationException() {
        super();
    }
    public TokenAuthorizationException(String message) {
        super(message);
    }
}
