package controllers;

import model.ClientModel;
import serializable.Credentials;
import serializable.MessageType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * Contrôleur pour la fenêtre de login
 */
public class LFController extends MouseAdapter implements DocumentListener {

    private ClientModel model;
    private String nickName;
    private String password;

    public LFController(ClientModel model) {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            Component frame = SwingUtilities.getRoot(button);

            if (nickName == null || password == null ) {
                JOptionPane.showMessageDialog(frame, "Please enter a nickname, a password and confirm your password.",
                        "Error", JOptionPane.OK_OPTION);
            } else {
                if (model.isInitialized()) {
                    if (model.isConnected()) {

                        model.sendToServer(new Credentials(nickName, password), MessageType.LOGIN);

                        JFrame jFrame = (JFrame) frame;
                        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
                    } else {

                        System.out.println("[*] Connection was lost, attempting to connect again");
                        model.connectToServer();
                        model.sendToServer(new Credentials(nickName, password), MessageType.LOGIN);

                        JFrame jFrame = (JFrame) frame;
                        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
                    }
                } else {
                    System.out.println("[!] Can't login if not connected to a server.");
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        try {
            String documentName = (String) e.getDocument().getProperty("parent");
            switch (documentName) {
                case "nickName":
                    nickName = e.getDocument().getText(0, e.getDocument().getLength());
                    break;
                case "pwd":
                    password = e.getDocument().getText(0, e.getDocument().getLength());
                    break;
                default:
                    break;

            }
        } catch (Exception ex) {
            System.out.println("[!] error while entering nick or pwd");
        }
    }
}