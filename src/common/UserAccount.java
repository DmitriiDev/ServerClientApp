package common;

import java.util.ArrayList;

public class UserAccount {


    private String login;
    private String pass;
    private String nick;
    private ArrayList<String> historyOfMessages;

    public  ArrayList<String> getHistoryOfMessages() {
        return historyOfMessages;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getNick() {
        return nick;
    }

    public UserAccount(){
    }

    public UserAccount(String login, String pass, String nick, ArrayList<String> historyOfMessages ) {
        this.login = login;
        this.pass = pass;
        this.nick = nick;
        this.historyOfMessages = historyOfMessages;
    }
}
