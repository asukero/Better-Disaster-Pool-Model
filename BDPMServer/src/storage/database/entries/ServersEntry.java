package storage.database.entries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServersEntry extends Entry{
    private String name;
    private String ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static ServersEntry parseResults(ResultSet data) throws SQLException {
        ServersEntry result = new ServersEntry();
        result.setId(data.getInt("id"));
        result.setName(data.getString("name"));
        result.setIp(data.getString("ip"));
        return result;
    }
}
