package serializable;

import java.io.Serializable;
import java.net.InetAddress;

public final class Message implements Serializable {
    private MessageType messageType;
    private Object content;
    private String senderName;
    private InetAddress senderIp;

    public Message(MessageType messageType, Object content, String senderName, InetAddress senderIp) {
        this.messageType = messageType;
        this.content = content;
        this.senderName = senderName;
        this.senderIp = senderIp;
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
