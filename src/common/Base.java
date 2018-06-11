package common;

import java.util.ArrayList;

public class Base implements AuthService {
    class Entry{
        private String login;
        private String pass;
        private String nick;

        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }

    private ArrayList<Entry> entries;

    public Base() {
        this.entries = new ArrayList<>();
        this.entries.add(new Entry("login1", "pass1", "Vasya"));
        this.entries.add(new Entry("login2", "pass2", "Petya"));
        this.entries.add(new Entry("login3", "pass3", "Katya"));
    }

    @Override
    public String getNickByCredentials(String login, String password) {
        for(Entry entry : entries){
            if(entry.login.equals(login) && entry.pass.equals(password))
                return entry.nick;
        }
        return null;
    }
}
