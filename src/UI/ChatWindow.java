//import com.sun.tools.javac.comp.Enter;
package UI;
import common.ServerConst;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;

public class ChatWindow extends JFrame implements ServerConst {

    private final static String newline = "\n";
    private JTextField message;
    private JTextArea chatHistory;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public ChatWindow(){

        try {
             this.socket = new Socket(SERVER_URL,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageIcon img = new ImageIcon("java.png");
        setTitle("Chat");
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 50));

        message = new JTextField(1);
        message.setMinimumSize(new Dimension(50, 25));
        message.setPreferredSize(new Dimension(200, 50));
        message.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        message.getDocument().putProperty("filterNewlines", Boolean.TRUE);

        chatHistory = new JTextArea();
        chatHistory.setLineWrap(true);
        chatHistory.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(chatHistory);

        JButton sendButton = new JButton("Send");
        message.addKeyListener
                (new KeyAdapter() {
                     public void keyPressed(KeyEvent e) {
                         int key = e.getKeyCode();
                         if (key == KeyEvent.VK_ENTER) {
                             String text = message.getText().trim();
                             if (text.length() > 0) {
                                 chatHistory.append(text+newline);
                                 message.selectAll();
                                 message.setText("");
                                 message.requestFocusInWindow();
                             }
                         }
                     }
                 }
                );

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = message.getText().trim();
                if (text.length() > 0) {
                    chatHistory.append(text+newline);
                    message.selectAll();
                    message.setText("");
                    message.requestFocusInWindow();
                }
            }
        });

        panel.add(sendButton, BorderLayout.EAST);
        panel.add(message, BorderLayout.CENTER);

        add(jScrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
        message.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new ChatWindow();
    }
}