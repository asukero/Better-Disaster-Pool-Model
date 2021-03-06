package serializable;

import java.io.Serializable;

/**
 * Identifiants envoyés au serveur dans un Message pour l'authentification
 */
public class Credentials implements Serializable {
    private String nickName;
    private String password;

    public Credentials(String nickName, String password) {
        this.nickName = nickName;
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }
}
