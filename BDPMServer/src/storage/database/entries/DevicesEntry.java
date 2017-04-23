package storage.database.entries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DevicesEntry  extends Entry{
    private String deviceName;
    private String ip;
    private int user;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public static DevicesEntry parseResults(ResultSet data) throws SQLException {
        DevicesEntry result = new DevicesEntry();
        result.setId(data.getInt("id"));
        result.setDeviceName(data.getString("name"));
        result.setIp(data.getString("ip"));
        result.setUser(data.getInt("user"));
        return result;
    }
}
