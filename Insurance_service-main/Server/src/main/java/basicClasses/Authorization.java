package basicClasses;

import java.io.Serializable;

public class Authorization implements Serializable {
    private int idUser;
    private String login;
    private String password;

    public Authorization() {}

    public Authorization(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Authorization(int idUser, String login, String password) {
        this.idUser = idUser;
        this.login = login;
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
