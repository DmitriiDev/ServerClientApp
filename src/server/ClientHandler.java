package server;

import common.ServerAPI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements ServerAPI {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickanme;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()->{
                try {
                    authLoop();
                    receiveLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receiveLoop() {

    }

    private void authLoop() throws IOException {
        while (true){
            String msg = in.readUTF();
            if(msg.startsWith(AUTH)){
                String[] elements = msg.split(" ");
                String nick = server.getAuthService().getNickByCredentials(elements[1],elements[2]);
                if(nick != null){
                    sendMessage(AUTH_SUCCESSFUL + " " + nick);
                    this.nickanme = nick;
                    server.broadcast(this.nickanme + " has entered the chat room.");
                    break;
                }else{
                    sendMessage("Wrong login/password");
                }
            }else {
                sendMessage("Authorize first!");
            }
        }
    }

    public void sendMessage(String str){
        try {
            out.writeUTF(str);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
