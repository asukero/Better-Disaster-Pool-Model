package storage.database.managers;

import storage.StorageEngine;
import storage.database.entries.DevicesEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DevicesManager extends Manager<DevicesEntry> {
    public DevicesManager(StorageEngine engine) throws SQLException {
        super(engine);
    }

    protected void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Devices(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "deviceName TEXT NOT NULL, " +
                "ip TEXT NOT NULL UNIQUE," +
                "user INTEGER NOT NULL REFERENCES Users(id));";

        Statement st = getConnection().createStatement();
        st.execute(query);
        st.close();
    }

    public boolean addEntry(DevicesEntry entry) throws SQLException {
        String query = "INSERT INTO Devices(deviceName, ip, user) " +
                "VALUES (?, ?, ?);";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, entry.getDeviceName());
        statement.setString(2, entry.getIp());
        statement.setInt(3, entry.getUser());

        boolean result = statement.execute();
        updateEntryId(entry, statement);
        statement.close();
        return result;
    }

    public List<DevicesEntry> getEntries() throws SQLException {
        String query = "SELECT id, deviceName, ip, user FROM Devices;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet data = statement.executeQuery();

        List<DevicesEntry> results = new ArrayList<>();
        while (data.next()) {
            results.add(DevicesEntry.parseResults(data));
        }

        data.close();
        statement.close();

        return results;
    }

    public DevicesEntry getEntry(int id) throws SQLException {
        String query = "SELECT id, deviceName, ip, user FROM Devices WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet data = statement.executeQuery();
        data.next();

        DevicesEntry entry = DevicesEntry.parseResults(data);
        data.close();
        statement.close();

        return entry;
    }

    public boolean updateEntry(DevicesEntry entry) throws SQLException {
        String query = "UPDATE Devices SET deviceName = ?, ip = ?, user = ? WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, entry.getDeviceName());
        statement.setString(2, entry.getIp());
        statement.setInt(3, entry.getUser());

        boolean result = statement.execute();
        statement.close();
        return result;
    }

    public boolean deleteEntry(DevicesEntry entry) throws SQLException {
        String query = "DELETE FROM Devices WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setInt(1, entry.getId());
        boolean result = statement.execute();
        statement.close();
        return result;
    }
}
