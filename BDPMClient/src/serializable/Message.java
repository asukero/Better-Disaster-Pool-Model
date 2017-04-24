package serializable;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message implements Serializable {
    private MessageType messageType;
    private Object content;
    private String senderName;
    private InetAddress senderIp;

    public Message(MessageType messageType, Object content) throws UnknownHostException {
        this.messageType = messageType;
        this.content = content;
        this.senderName = InetAddress.getLocalHost().getHostName();
        this.senderIp = InetAddress.getLocalHost();
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

