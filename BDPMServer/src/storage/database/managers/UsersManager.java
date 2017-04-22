package storage.database.managers;

import storage.StorageEngine;
import storage.database.entries.DevicesEntry;
import storage.database.entries.UsersEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsersManager extends Manager<UsersEntry> {
    public UsersManager(StorageEngine engine) throws SQLException {
        super(engine);
    }

    protected void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nickName TEXT NOT NULL UNIQUE, " +
                "hash TEXT NOT NULL);";

        Statement st = getConnection().createStatement();
        st.execute(query);
        st.close();
    }

    public boolean addEntry(UsersEntry entry) throws SQLException {
        String query = "INSERT INTO Users(nickName, hash) " +
                "VALUES (?, ?);";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, entry.getNickName());
        statement.setString(2, entry.getHash());

        boolean result = statement.execute();
        updateEntryId(entry, statement);
        statement.close();
        return result;
    }

    public List<UsersEntry> getEntries() throws SQLException {
        String query = "SELECT id, nickName, hash FROM Users;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet data = statement.executeQuery();

        List<UsersEntry> results = new ArrayList<>();
        while(data.next()) {
            results.add(UsersEntry.parseResults(data));
        }

        data.close();
        statement.close();

        return results;
    }

    public UsersEntry getEntry(int id) throws SQLException {
        String query = "SELECT id, nickName, hash FROM Users WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet data = statement.executeQuery();
        data.next();

        UsersEntry entry = UsersEntry.parseResults(data);
        data.close();
        statement.close();

        return entry;
    }

    public boolean updateEntry(UsersEntry entry) throws SQLException {
        String query = "UPDATE Users SET nickName = ?, hash = ? WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, entry.getNickName());
        statement.setString(2, entry.getHash());
        statement.setInt(3, entry.getId());

        boolean result = statement.execute();
        statement.close();
        return result;
    }

    public boolean deleteEntry(UsersEntry entry) throws SQLException {
        String query = "DELETE FROM Users WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setInt(1, entry.getId());
        boolean result = statement.execute();
        statement.close();
        return result;
    }


    public List<DevicesEntry> getDevices(int user) throws SQLException {
        String query = "SELECT id, deviceName, ip, user FROM Devices WHERE user = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, user);
        ResultSet data = statement.executeQuery();
        data.next();

        List<DevicesEntry> results = new ArrayList<>();
        while (data.next()) {
            results.add(DevicesEntry.parseResults(data));
        }
        data.close();
        statement.close();

        return results;
    }

}
