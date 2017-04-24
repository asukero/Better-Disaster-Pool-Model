package serializable;

import java.net.UnknownHostException;

public class ReturnMessage extends Message {
    boolean isSuccess;

    public ReturnMessage(MessageType messageType, Object content, boolean isSuccess) throws UnknownHostException {
        super(messageType, content);
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
