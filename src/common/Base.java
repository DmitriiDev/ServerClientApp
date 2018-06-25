package common;

import java.util.ArrayList;
import java.util.Vector;

public class Base implements AuthService {

    private Vector<String> namesOfUsers = new Vector<>();

    private ArrayList<UserAccount> users;

    public ArrayList<UserAccount> getUsers() {
        return users;
    }

    public Base() {
        this.users = new ArrayList<>();
        this.users.add(new UserAccount("login1", "pass1", "Vasya", new ArrayList<String>()));
        this.users.add(new UserAccount("login2", "pass2", "Petya", new ArrayList<String>()));
        this.users.add(new UserAccount("login3", "pass3", "Katya", new ArrayList<String>()));
    }

    public Vector<String> listOfNames() {
        for (UserAccount user : users) {
            namesOfUsers.add(user.getNick());
        }
        return namesOfUsers;
    }

    public UserAccount getUserByNick(String nick){
        for(UserAccount user : users){
            if(nick.equals(user.getNick())){
                return user;
            }
        }
        return null;
    }

    @Override
    public String getNickByCredentials(String login, String password) {
        for (UserAccount user : users) {
            if (user.getLogin().equals(login) && user.getPass().equals(password))
                return user.getNick();
        }
        return null;
    }
}
