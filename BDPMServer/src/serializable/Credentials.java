package serializable;

import java.io.Serializable;

public class Credentials implements Serializable {
    private String nickName;
    private String hash;

    public Credentials(String nickName, String hash) {
        this.nickName = nickName;
        this.hash = hash;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
