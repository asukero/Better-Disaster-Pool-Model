package view;

import view.utils.UserInputFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class LoginFrame extends JFrame {
    static final String TITLE = "Login";

    private JTextField nickName = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JButton loginButton = new JButton("Login");

    public LoginFrame() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 300);
        setLayout(new BorderLayout(0, 0));

        JPanel mainPanel = new JPanel(new GridLayout(3,1,5,5));
        mainPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JPanel nickPanel = new JPanel(new FlowLayout());
        nickName.setHorizontalAlignment(SwingConstants.CENTER);
        nickName.setPreferredSize(new Dimension(150, 20));
        nickName.getDocument().putProperty("parent", "nickName");
        AbstractDocument nickDoc = (AbstractDocument) nickName.getDocument();
        nickDoc.setDocumentFilter(new UserInputFilter());
        JLabel nickLabel = new JLabel("Enter NickName");
        nickPanel.add(nickLabel);
        nickPanel.add(nickName);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        password.setHorizontalAlignment(SwingConstants.CENTER);
        password.setPreferredSize(new Dimension(150, 20));
        password.getDocument().putProperty("parent", "pwd");
        AbstractDocument pwdDoc = (AbstractDocument) password.getDocument();
        pwdDoc.setDocumentFilter(new UserInputFilter());
        JLabel pwdLabel = new JLabel("Enter password");
        passwordPanel.add(pwdLabel);
        passwordPanel.add(password);
        JPanel loginPanel = new JPanel(new FlowLayout());
        loginPanel.add(loginButton);

        mainPanel.add(nickPanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(loginPanel);

        add(mainPanel);

        setVisible(true);

    }

    public JTextField getNickName() {
        return nickName;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}