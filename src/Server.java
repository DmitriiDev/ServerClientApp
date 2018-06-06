import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static ServerSocket server = null;
    private static Socket socket = null;
    private static Scanner sc = null;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Waiting for connection...");
            socket = server.accept();
            System.out.println("Server created");
            sc = new Scanner(System.in);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(rRecieve()).start();
            new Thread(rSend()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Runnable rRecieve() {
        Runnable rRecieve = () -> {
            try {
                while (true) {
                    System.out.println("Client: " + in.readUTF());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
       return rRecieve;
    }

    public Runnable rSend() {
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
    }

    public static void main(String[] args) {

        try {
            Server server = new Server(8988);
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
