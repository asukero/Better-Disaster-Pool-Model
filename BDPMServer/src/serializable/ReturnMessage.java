package serializable;

/**
 * classe enfant de Message représentant un message de retour, généralement envoyé du serveur vers le client.
 * Permet de déterminer si la demande du client a été correct traitée par le serveur ou non (isSuccess)
 */
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
