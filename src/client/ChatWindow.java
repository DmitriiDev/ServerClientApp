//import com.sun.tools.javac.comp.Enter;
package client;

import common.ServerConst;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ChatWindow extends JFrame implements ServerConst {

    private final static String newline = "\n";
    private JTextField message;
    private JTextArea chatHistory;

    JPanel top;
    JPanel bottom;

    JTextField login;
    JPasswordField password;
    JButton auth;

    private ClientConnection clientConnection;


    public ChatWindow() {
        clientConnection = new ClientConnection();
        clientConnection.init(this);

        ImageIcon img = new ImageIcon("java.png");
        setTitle("Chat");
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());

        bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(300, 50));

        message = new JTextField(1);
        message.setMinimumSize(new Dimension(50, 25));
        message.setPreferredSize(new Dimension(200, 50));
        message.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        message.getDocument().putProperty("filterNewlines", Boolean.TRUE);

        chatHistory = new JTextArea();
        chatHistory.setLineWrap(true);
        chatHistory.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(chatHistory);

        login = new JTextField();
        password = new JPasswordField();
        auth = new JButton("Log in");
        top = new JPanel(new GridLayout(1, 3));
        top.add(login);
        top.add(password);
        top.add(auth);


        JButton sendButton = new JButton("Send");
        message.addKeyListener
                (new KeyAdapter() {
                     public void keyPressed(KeyEvent e) {
                         int key = e.getKeyCode();
                         if (key == KeyEvent.VK_ENTER) {
                             String text = message.getText().trim();
                             if (text.length() > 0) {
                                 chatHistory.append(text + newline);
                                 message.selectAll();
                                 message.setText("");
                                 message.requestFocusInWindow();
                             }
                         }
                     }
                 }
                );

        sendButton.addActionListener(e -> {
            String text = message.getText().trim();
            if (text.length() > 0) {
                chatHistory.append(text + newline);
                message.selectAll();
                message.setText("");
                message.requestFocusInWindow();
            }
        });

        auth.addActionListener(e -> auth());
        password.addActionListener(e -> auth());

        bottom.add(sendButton, BorderLayout.EAST);
        bottom.add(message, BorderLayout.CENTER);

        add(jScrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        setLocationRelativeTo(null);
        setVisible(true);
        message.requestFocusInWindow();
    }

    private void auth() {
        clientConnection.auth(login.getText(), new String(password.getPassword()));
    }

    public void switchWindows() {
        top.setVisible(!clientConnection.isAuthorized());
        bottom.setVisible(clientConnection.isAuthorized());
    }

    public static void main(String[] args) {
        new ChatWindow();
    }

    public void showMessage(String msg) {
        chatHistory.append(msg + "\n");
        chatHistory.moveCaretPosition(chatHistory.getDocument().getLength());
    }
}