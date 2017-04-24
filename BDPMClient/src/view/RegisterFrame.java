package view;

import view.utils.UserInputFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

/**
 * fenêtre pour l'inscription d'un nouvel utilisateur
 */
public class RegisterFrame extends JFrame {
    static final String TITLE = "Register";

    private JTextField nickName = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JPasswordField cPassword = new JPasswordField();
    private JButton registerButton = new JButton("Register");

    public RegisterFrame() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 300);
        setLayout(new BorderLayout(0, 0));

        JPanel mainPanel = new JPanel(new GridLayout(4,1,5,5));
        mainPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        JPanel nickPanel = new JPanel(new FlowLayout());
        nickName.setHorizontalAlignment(SwingConstants.CENTER);
        nickName.setPreferredSize(new Dimension(150, 20));
        nickName.getDocument().putProperty("parent", "nickName");
        AbstractDocument nickDoc = (AbstractDocument) nickName.getDocument();
        nickDoc.setDocumentFilter(new UserInputFilter());
        JLabel nickLabel = new JLabel("Enter a NickName");
        nickPanel.add(nickLabel);
        nickPanel.add(nickName);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        password.setHorizontalAlignment(SwingConstants.CENTER);
        password.setPreferredSize(new Dimension(150, 20));
        password.getDocument().putProperty("parent", "pwd");
        AbstractDocument pwdDoc = (AbstractDocument) password.getDocument();
        pwdDoc.setDocumentFilter(new UserInputFilter());
        JLabel pwdLabel = new JLabel("Enter a password");
        passwordPanel.add(pwdLabel);
        passwordPanel.add(password);

        JPanel cPasswordPanel = new JPanel(new FlowLayout());
        cPassword.setHorizontalAlignment(SwingConstants.CENTER);
        cPassword.setPreferredSize(new Dimension(150, 20));
        cPassword.getDocument().putProperty("parent", "cPwd");
        AbstractDocument cPwdDoc = (AbstractDocument) cPassword.getDocument();
        cPwdDoc.setDocumentFilter(new UserInputFilter());
        JLabel cPwdLabel = new JLabel("Confirm password");
        cPasswordPanel.add(cPwdLabel);
        cPasswordPanel.add(cPassword);

        JPanel registerPanel = new JPanel(new FlowLayout());
        registerPanel.add(registerButton);

        mainPanel.add(nickPanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(cPasswordPanel);
        mainPanel.add(registerPanel);

        add(mainPanel);

        setVisible(true);

    }

    public JTextField getNickName() {
        return nickName;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public JPasswordField getcPassword() {
        return cPassword;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}
