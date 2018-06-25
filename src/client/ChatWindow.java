//import com.sun.tools.javac.comp.Enter;
package client;

import common.Base;
import common.ServerConst;
import common.UserAccount;
import server.Server;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ChatWindow extends JFrame implements ServerConst {

    private final static String newline = "\n";
    private JTextField message;
    private JTextArea chatHistory;

    JPanel top;
    JPanel bottom;
    JPanel leftMenu;

    JTextField login;
    JPasswordField password;
    JButton auth;
    JList jList;

    private ClientConnection clientConnection;
    private Map<String, String> historyOfUsers = new HashMap<String, String>();
    ;


    public ChatWindow() {
        Base base = new Base();
        clientConnection = new ClientConnection();
        clientConnection.init(this);

        ImageIcon img = new ImageIcon("java.png");
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

        leftMenu = new JPanel();
        leftMenu.setLayout(new BorderLayout());

        jList = new JList(base.listOfNames());
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setVisibleRowCount(-1);
        jList.setPreferredSize(new Dimension(75, 50));
        leftMenu.add(jList, BorderLayout.CENTER);

        jList.getSelectionModel().addListSelectionListener(e -> {
            valueChanged(e);
        });

        JButton sendButton = new JButton("Send");
        message.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());

        auth.addActionListener(e -> auth());
        password.addActionListener(e -> auth());
        bottom.add(sendButton, BorderLayout.EAST);
        bottom.add(message, BorderLayout.CENTER);

        add(jScrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.EAST);
        setLocationRelativeTo(null);
        setVisible(true);
        message.requestFocusInWindow();
    }

    private void sendMessage() {
        if (getNameFromList() != null) {
            String msg = "/p " + getNameFromList() + " " + message.getText();
            message.setText("");
            clientConnection.sendMessage(msg);
        } else {
            String msg = message.getText();
            message.setText("");
            clientConnection.sendMessage(msg);
        }
    }

    private void auth() {
        clientConnection.auth(login.getText(), new String(password.getPassword()));
        Base base = new Base();
        setTitle(base.getNickByCredentials(login.getText(), password.getPassword().toString()));
    }

    public void switchWindows() {
        top.setVisible(!clientConnection.isAuthorized());
        bottom.setVisible(clientConnection.isAuthorized());
    }

    public void valueChanged(ListSelectionEvent e) {
        String messageHistoryOfuser = chatHistory.getText();
        chatHistory.setText("");
        Base base = new Base();
        int selected = jList.getSelectedIndex();
        int previous = selected == e.getFirstIndex() ? e.getLastIndex() : e.getFirstIndex();
        Vector<String> listOfNames = base.listOfNames();
        String previousName = listOfNames.get(previous);
        String currentName = listOfNames.get(selected);
        System.out.println("prev " + previousName + "| current" + currentName);
        if (messageHistoryOfuser.length() > 0) {
            historyOfUsers.put(previousName, messageHistoryOfuser);
        }
        System.out.println(historyOfUsers.size());
        for (Map.Entry seekOfUserHistory : historyOfUsers.entrySet()) {
            if (seekOfUserHistory.getKey().equals(currentName)) {
                chatHistory.append((String) seekOfUserHistory.getValue());
            }
        }


    }


    public String getNameFromList() {
        String pickedName = (String) jList.getSelectedValue();
        return pickedName;
    }

    public void showMessage(String msg) {
        chatHistory.append(msg + "\n");
        chatHistory.moveCaretPosition(chatHistory.getDocument().getLength());
    }

    public static void main(String[] args) {
        buildClientByNumber(1);
        buildClientByNumber(2);
        buildClientByNumber(3);

    }

    private static void buildClientByNumber(int i) {
        Base base = new Base();
        ChatWindow cw = new ChatWindow();
        cw.login.setText("login" + i);
        cw.password.setText("pass" + i);
        cw.auth.doClick();
        cw.setTitle(base.getNickByCredentials(cw.login.getText(), new String(cw.password.getPassword())));
    }
}