package controllers;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonController extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() instanceof JButton){
            JButton button = (JButton)e.getSource();
            if(button.getText() == "Connect"){
                System.out.println("Connecting to server...");
            } else if (button.getText() == "Disconnect"){
                System.out.println("Disconnecting to server...");
                System.out.println("Done.");
            }
        }
    }
}
