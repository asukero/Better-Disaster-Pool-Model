package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.PrintStream;
import java.text.ParseException;

public class MainFrame extends JFrame {
    static final String TITLE = "BDPM Client";

    private JFormattedTextField ipField;
    private JButton connectButton = new JButton("Connect");
    private JButton disconnectButton = new JButton("Disconnect");


    public MainFrame() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout(0, 0));

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        mainPanel.setBorder(new EmptyBorder(100,10,100,10));

        try {
            JPanel serverPanel = new JPanel(new FlowLayout());
            JLabel text = new JLabel("Server IP");
            MaskFormatter ipmask = new MaskFormatter("###.###.###.###");
            ipmask.setPlaceholderCharacter(' ');
            ipField = new JFormattedTextField(ipmask);
            ipField.setHorizontalAlignment(SwingConstants.CENTER);
            ipField.setPreferredSize(new Dimension(150,20));

            serverPanel.add(text);
            serverPanel.add(ipField);

            JPanel buttonsPanel = new JPanel(new FlowLayout());

            buttonsPanel.add(connectButton);
            buttonsPanel.add(disconnectButton);

            JPanel logPanel = new JPanel(new BorderLayout(5,5));
            initLogger(logPanel);

            mainPanel.add(serverPanel);
            mainPanel.add(buttonsPanel);
            mainPanel.add(logPanel);
            add(mainPanel);

        } catch (ParseException ex) {

        }
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
}
