package pool;

public class User {
    private String nickName;
    private boolean isConnected;
    private String deviceName;

    public User(String nickName, boolean isConnected, String deviceName) {
        this.nickName = nickName;
        this.isConnected = isConnected;
        this.deviceName = deviceName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
