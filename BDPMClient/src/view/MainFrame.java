package view;

import view.utils.IPAddressFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.PrintStream;

public class MainFrame extends JFrame {
    static final String TITLE = "BDPM Client";

    private JFormattedTextField ipField;
    private JButton connectButton = new JButton("Connect");
    private JButton disconnectButton = new JButton("Disconnect");
    private JButton helpButton = new JButton("Help");
    private JButton registerButton = new JButton("Register");
    private JFormattedTextField portField;


    public MainFrame() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout(0, 0));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(50, 10, 50, 10));

        JPanel serverPanel = new JPanel(new GridLayout(2, 1));

        ipField = new JFormattedTextField(new IPAddressFormatter());
        ipField.setHorizontalAlignment(SwingConstants.CENTER);
        ipField.setPreferredSize(new Dimension(150, 20));
        ipField.getDocument().putProperty("parent", "ip");

        NumberFormatter nf = new NumberFormatter();
        nf.setMinimum(new Integer(1025));
        nf.setMaximum(new Integer(65535));
        portField = new JFormattedTextField(nf);
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        portField.setPreferredSize(new Dimension(50, 20));
        portField.getDocument().putProperty("parent", "port");

        JPanel ipPanel = new JPanel(new FlowLayout());
        ipPanel.add(new JLabel("Server IP"));
        ipPanel.add(ipField);

        JPanel portPanel = new JPanel(new FlowLayout());
        portPanel.add(new JLabel("Port number"));
        portPanel.add(portField);

        serverPanel.add(ipPanel);
        serverPanel.add(portPanel);


        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(connectButton);
        buttonsPanel.add(disconnectButton);

        JPanel registerPanel = new JPanel(new FlowLayout());
        registerPanel.add(registerButton);


        JPanel logPanel = new JPanel(new BorderLayout(5, 5));
        initLogger(logPanel);
        JPanel northPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        northPanel.add(serverPanel);
        northPanel.add(buttonsPanel);
        northPanel.add(registerPanel);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(logPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(helpButton);

        mainPanel.add(controlPanel, BorderLayout.PAGE_END);

        add(mainPanel);
        setVisible(true);

    }

    private void initLogger(JPanel bottomPanel) {
        EventQueue.invokeLater(() -> {
            LogPanel logPanel = new LogPanel();
            bottomPanel.add(logPanel);

            PrintStream ps = System.out;
            System.setOut(new PrintStream(new StreamLogger("STDOUT", logPanel, ps)));
        });
    }

    public JFormattedTextField getIpField() {
        return ipField;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JButton getDisconnectButton() {
        return disconnectButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public JFormattedTextField getPortField() {
        return portField;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}
