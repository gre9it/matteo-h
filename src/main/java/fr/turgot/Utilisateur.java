package fr.turgot;

import java.io.Serializable;


public class Utilisateur implements Serializable {

    private String login;
    private String password;

    public Utilisateur() {
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
