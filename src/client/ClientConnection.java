package client;

import common.ServerAPI;
import common.ServerConst;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientConnection implements ServerConst, ServerAPI {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    boolean isAuthorized = false;


    public ClientConnection() {
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public void init(ChatWindow view) {
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() ->
        {
            try {
                authLoop(view);
                receiveMessage(view);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void authLoop(ChatWindow view) throws IOException {
        while (true) {
            String msg = in.readUTF();
            if (msg.startsWith(AUTH_SUCCESSFUL)) {
                setAuthorized(true);
                view.switchWindows();
                break;
            }
            view.showMessage(msg);
        }
    }

    private void receiveMessage(ChatWindow view) throws IOException {
        while (true) {
            String msg = in.readUTF();
            String[] elements = msg.split(" ");
            if (msg.startsWith(SYSTEM_SYMBOL)) {

                if (elements[0].equals(CLOSE_CONNECTION)) {
                    setAuthorized(false);
                    view.showMessage("Connection closed");
                    view.switchWindows();
                }
                if(msg.startsWith(USER_LIST)) {
                    String[]users = msg.split(" ");
                    String [] newArray = Arrays.copyOfRange(users, 1, users.length);
                    Arrays.sort(newArray);
                    view.showUserList(newArray);
                }

                setAuthorized(true);
                view.switchWindows();
                break;

            } else {
                view.showMessage(msg);
            }
        }
    }

    public void sendMessage(String str) {
        try {
            out.writeUTF(str);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void auth(String login, String pass) {
        try {
            out.writeUTF(AUTH + " " + login + " " + pass);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            out.writeUTF(CLOSE_CONNECTION);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
