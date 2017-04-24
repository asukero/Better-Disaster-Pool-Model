package serializable;

public class ReturnMessage extends Message {
    boolean isSuccess;

    public ReturnMessage(MessageType messageType, Object content, boolean isSuccess) {
        super(messageType, content);
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
