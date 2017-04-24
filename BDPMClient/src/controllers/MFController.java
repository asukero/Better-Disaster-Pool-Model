package controllers;

import model.ClientModel;
import serializable.MessageType;
import view.LoginFrame;
import view.RegisterFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;

/**
 * Contrôleur de la fenêtre principale
 */
public class MFController extends MouseAdapter implements DocumentListener {

    private ClientModel model;
    private String port;
    private String ipAddress;

    public MFController(ClientModel model) {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            //Gestion des boutons
            switch (button.getText()) {
                case "Connect":
                    try {
                        model.initialize(InetAddress.getByName(ipAddress), Integer.parseInt(port));
                        model.sendToServer("CONNECT", MessageType.CONNECT);
                    } catch (Exception ex) {
                        System.out.println("[!] Ip address or port are not valid");
                    }
                    break;
                case "Disconnect":
                    if (model.isInitialized()) {
                        if (model.isConnected()) {
                            System.out.println("[*] Disconnecting from server...");
                            model.sendToServer("DISCONNECT", MessageType.DISCONNECT);
                            model.closeConnection();
                        } else {
                            System.out.println("[!] connection has already been lost!");
                        }
                    } else {
                        System.out.println("[!] Disconnection is not possible if not connected to a server.");
                    }

                    break;
                case "Help":
                    if (model.isInitialized()) {
                        System.out.println("[*] Requesting Help...");
                        if (model.isConnected()) {
                            if (model.isAuthenticated()) {
                                model.sendToServer("HELP", MessageType.HELP);
                            } else {
                                System.out.println("[!] Can't request for help if not log on the server.");
                            }

                        } else {
                            System.out.println("[*] Connection was lost, attempting to connect again");
                            model.connectToServer();
                            if (model.isAuthenticated()) {
                                model.sendToServer("HELP", MessageType.HELP);
                            } else {
                                System.out.println("[!] Can't request for help if not log on the server.");
                            }
                        }
                    } else {
                        System.out.println("[!] Can't request for help if not connected to a server.");
                    }
                    break;
                case "Helper":
                    if (model.isInitialized()) {
                        System.out.println("[*] Requesting to wait to help...");
                        if (model.isConnected()) {
                            if (model.isAuthenticated()) {
                                model.sendToServer("HELPER", MessageType.HELPER);
                                model.waitingToHelp();
                            } else {
                                System.out.println("[!] Can't request to help if not log on the server.");
                            }

                        } else {
                            System.out.println("[*] Connection was lost, attempting to connect again");
                            model.connectToServer();
                            if (model.isAuthenticated()) {
                                model.sendToServer("HELPER", MessageType.HELPER);
                                model.waitingToHelp();
                            } else {
                                System.out.println("[!] Can't request to help if not log on the server.");
                            }
                        }
                    } else {
                        System.out.println("[!] Can't request to help if not connected to a server.");
                    }
                    break;
                case "Register":
                    if (model.isInitialized()) {
                        if (model.isConnected()) {
                            RegisterFrame rf = new RegisterFrame();
                            RFController rfController = new RFController(model);
                            rf.getNickName().getDocument().addDocumentListener(rfController);
                            rf.getPassword().getDocument().addDocumentListener(rfController);
                            rf.getcPassword().getDocument().addDocumentListener(rfController);
                            rf.getRegisterButton().addMouseListener(rfController);
                        } else {
                            System.out.println("[*] Connection was lost, attempting to connect again");
                            model.connectToServer();
                        }
                    } else {
                        System.out.println("[!] Can't register if not connect to a server!");
                    }
                    break;
                case "Login":
                    if (model.isInitialized()) {
                        if (model.isConnected()) {
                            LoginFrame lf = new LoginFrame();
                            LFController lfController = new LFController(model);
                            lf.getNickName().getDocument().addDocumentListener(lfController);
                            lf.getPassword().getDocument().addDocumentListener(lfController);
                            lf.getLoginButton().addMouseListener(lfController);
                        } else {
                            System.out.println("[*] Connection was lost, attempting to connect again");
                            model.connectToServer();
                        }
                    } else {
                        System.out.println("[!] Can't register if not connect to a server!");
                    }
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
        //gestion des textfields
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
