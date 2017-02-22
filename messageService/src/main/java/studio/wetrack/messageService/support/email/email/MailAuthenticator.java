package studio.wetrack.messageService.support.email.email;

import javax.mail.Authenticator;

public class MailAuthenticator extends Authenticator {
    private String username = null;
    private String userpasswd = null;

    public MailAuthenticator(){}
    public MailAuthenticator(String username,String userpasswd){
        this.username = username;
        this.userpasswd = userpasswd;
    }

    public void setUserName(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.userpasswd = password;
    }

    public javax.mail.PasswordAuthentication getPasswordAuthentication(){
        return new javax.mail.PasswordAuthentication(username,userpasswd);
    }

}
