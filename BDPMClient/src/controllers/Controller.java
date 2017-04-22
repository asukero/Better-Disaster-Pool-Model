package controllers;

import model.ClientModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;

public class Controller extends MouseAdapter implements DocumentListener {

    private ClientModel model;
    private String port;
    private String ipAddress;

    public Controller(ClientModel model) {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            switch (button.getText()) {
                case "Connect":
                    try {
                        model.initialize(InetAddress.getByName(ipAddress), Integer.parseInt(port));
                        model.sendToServer("CONNECT");
                    } catch (Exception ex) {
                        System.out.println("[!] Ip address or port are not valid");
                    }
                    break;
                case "Disconnect":
                    if (model.isInitialized()) {
                        if (model.isConnected()) {
                            System.out.println("[*] Disconnecting from server...");
                            model.sendToServer("DISCONNECT");
                            model.closeConnection();
                        } else {
                            System.out.println("[!] connection has already been lost!");
                        }
                    } else {
                        System.out.println("[!] Deconnection is not possible if not connected to a server.");
                    }

                    break;
                case "Help":
                    if (model.isInitialized()) {
                        System.out.println("[*] Requesting Help...");
                        if (model.isConnected()) {
                            model.sendToServer("HELP");
                        } else {
                            System.out.println("[*] Connection was lost, attempting to connect again");
                            model.connectToServer();
                            model.sendToServer("[*]HELP");
                        }
                    } else {
                        System.out.println("[!] Can't request for help if not connected to a server.");
                    }


                    break;
                default:
                    break;
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
            if (e.getDocument().getProperty("parent") == "ip") {
                ipAddress = e.getDocument().getText(0, e.getDocument().getLength());
            } else if (e.getDocument().getProperty("parent") == "port") {
                port = e.getDocument().getText(0, e.getDocument().getLength());
                port = port.replaceAll("\u00a0", "");
            }
        } catch (Exception ex) {
            System.out.println("[!] error while entering ip or port");
        }
    }
}
