package storage.database.entries;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersEntry extends Entry {
    private String nickName;
    private String hash;

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

    public static UsersEntry parseResults(ResultSet data) throws SQLException {
        UsersEntry result = new UsersEntry();
        result.setId(data.getInt("id"));
        result.setNickName(data.getString("nickName"));
        result.setHash(data.getString("hash"));
        return result;
    }
}
