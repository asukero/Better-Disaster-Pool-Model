package storage.database.managers;

import storage.StorageEngine;
import storage.database.entries.ServersEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServersManager extends Manager<ServersEntry> {
    public ServersManager(StorageEngine engine) throws SQLException {
        super(engine);
    }

    protected void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Servers(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "name TEXT NOT NULL, " +
                "ip TEXT NOT NULL UNIQUE);";

        Statement st = getConnection().createStatement();
        st.execute(query);
        st.close();
    }

    public boolean addEntry(ServersEntry entry) throws SQLException {
        String query = "INSERT INTO Servers(name, ip) " +
                "VALUES (?, ?);";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, entry.getName());
        statement.setString(2, entry.getIp());

        boolean result = statement.execute();
        updateEntryId(entry, statement);
        statement.close();
        return result;
    }

    public List<ServersEntry> getEntries() throws SQLException {
        String query = "SELECT id, name, ip FROM Servers;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet data = statement.executeQuery();

        List<ServersEntry> results = new ArrayList<>();
        while(data.next()) {
            results.add(ServersEntry.parseResults(data));
        }

        data.close();
        statement.close();

        return results;
    }

    public ServersEntry getEntry(int id) throws SQLException {
        String query = "SELECT id, name, ip FROM Servers WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet data = statement.executeQuery();
        data.next();

        ServersEntry entry = ServersEntry.parseResults(data);
        data.close();
        statement.close();

        return entry;
    }

    public boolean updateEntry(ServersEntry entry) throws SQLException {
        String query = "UPDATE Servers SET name = ?, ip = ? WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, entry.getName());
        statement.setString(2, entry.getIp());
        statement.setInt(3, entry.getId());

        boolean result = statement.execute();
        statement.close();
        return result;
    }

    public boolean deleteEntry(ServersEntry entry) throws SQLException {
        String query = "DELETE FROM Servers WHERE id = ?;";
        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setInt(1, entry.getId());
        boolean result = statement.execute();
        statement.close();
        return result;
    }
}
