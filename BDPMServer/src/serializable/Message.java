package serializable;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Classe permettant d'envoyer des informations entre le client et le serveur
 */
public class Message implements Serializable {
    private MessageType messageType;
    private Object content;
    private String senderName;
    private InetAddress senderIp;

    public Message(MessageType messageType, Object content) {
        this.messageType = messageType;
        this.content = content;
        try {
            this.senderName = InetAddress.getLocalHost().getHostName();
            this.senderIp = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("[!] Error while creating message " + ex.getMessage());
        }
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public InetAddress getSenderIp() {
        return senderIp;
    }
}
