package server;

import common.ServerConst;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Server implements ServerConst {
    private ServerSocket mServer = null;
    private Socket mSocket = null;
    private Vector<ClientHandler> clients;
    private Map<String, String> credentials = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Server() {
        try {
            this.mServer = new ServerSocket(PORT);

            in = new DataInputStream(mSocket.getInputStream());
            out = new DataOutputStream(mSocket.getOutputStream());
            clients = new Vector<>();
            credentials = new HashMap<>();
            credentials.put("log0", "pas0");
            credentials.put("log1", "pas1");
            credentials.put("log2", "pas2");
            credentials.put("log3", "pas3");
            credentials.put("log3", "pas3");
            credentials.put("log3", "pas3");
            credentials.put("log3", "pas3");

            System.out.println("Waiting for connection...");
            int entry = 0;
            while (true) {
                mSocket = mServer.accept();
                clients.add(new ClientHandler(this, this.mSocket, credentials.get(entry++)));
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

    public void unicast(String msg, ClientHandler client) {
        if (client != null) {
            client.sendMessage(msg);
        } else {
            System.out.println("No such UI!");
        }

    }

//    public Runnable rRecieve() {
//        Runnable rRecieve = () -> {
//            try {
//                while (true) {
//                    System.out.println("Client: " + in.readUTF());
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//        return rRecieve;
//    }

/*    public Runnable rSend() {
        Runnable rRecieve = () -> {
            try {
                while (sc.hasNext()) {
                    out.writeUTF(sc.nextLine());
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return rRecieve;
    }*/

    public static void main(String[] args) {
        new Server();
    }
}
