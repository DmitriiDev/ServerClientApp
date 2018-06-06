import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Socket socket = null;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private static Scanner scanner = null;

    public Client(String nameHost, int port) {
        try {
            socket = new Socket(nameHost, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            scanner = new Scanner(System.in);

            new Thread(rReceive()).start();
            new Thread(rSend()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Runnable rReceive() {

        Runnable rReceive = () -> {
            try {
                while (true) {
                    System.out.println("Client: " + in.readUTF());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return rReceive;
    }

    public Runnable rSend() {
        Runnable rSend = () -> {
            try {
                while (scanner.hasNext()) {
                    out.writeUTF(scanner.nextLine());
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
        return rSend;
    }

    public static void main(String[] args) {
        Client client = new Client("localHost", 8988);
    }

}
