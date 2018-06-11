package server;

import common.AuthService;
import common.Base;
import common.ServerConst;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server implements ServerConst {
    private ServerSocket mServer = null;
    private Socket mSocket = null;
    private Vector<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        try {
            this.mServer = new ServerSocket(PORT);
            clients = new Vector<>();
            this.authService = new Base();
            System.out.println("Waiting for connection...");

            while (true) {
                mSocket = mServer.accept();
                clients.add(new ClientHandler(this, this.mSocket));
                System.out.println("connected");
            }

        } catch (IOException e) {
            try {
                this.mServer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void broadcast(String msgToAll) {
        for (ClientHandler client : clients) {
            client.sendMessage(msgToAll);
        }
    }

    public void unsubscribeMe(ClientHandler clientHandler) {
        this.clients.remove(clientHandler);
    }

    public void unicast(String msg, ClientHandler client) {
        if (client != null) {
            client.sendMessage(msg);
        } else {
            System.out.println("No such client!");
        }

    }

    public static void main(String[] args) {
        new Server();
    }
}
