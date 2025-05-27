package org.yavar007.database;

import org.yavar007.interfaces.IRoomDatabaseHelper;
import org.yavar007.models.UsersInRoomModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDatabaseHelper implements IRoomDatabaseHelper {
    private final Connection connection;
    private static final int MAX_USERS = 10;

    public RoomDatabaseHelper() {
        connection = Connect();
        createRoomsTable();
    }

    public boolean canAddUser() {
        return selectAllData().size() < MAX_USERS;
    }

    private Connection Connect() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data/data.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return c;
    }

    private void createRoomsTable() {
        String query = "CREATE TABLE IF NOT EXISTS UsersInRoom (" +
                "ID VARCHAR(255) PRIMARY KEY, " +
                "nickName VARCHAR(255), " +
                "OSName VARCHAR(255), " +
                "clientRole VARCHAR(255), " +
                "isOnline BOOLEAN, " +
                "lastAlive NUMERIC" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insertData(UsersInRoomModel client) {
        String sql = "INSERT INTO UsersInRoom (ID, nickName, OSName, clientRole, isOnline, lastAlive) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getID());
            pstmt.setString(2, client.getNickName());
            pstmt.setString(3, client.getOSName());
            pstmt.setString(4, client.getClientRole());
            pstmt.setBoolean(5, client.isOnline());
            pstmt.setLong(6, client.getLastAlive());
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateData(UsersInRoomModel client) {
        String sql = "UPDATE UsersInRoom SET nickName = ?, OSName = ?, clientRole = ?, isOnline = ?, lastAlive = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getNickName());
            pstmt.setString(2, client.getOSName());
            pstmt.setString(3, client.getClientRole());
            pstmt.setBoolean(4, client.isOnline());
            pstmt.setLong(5, client.getLastAlive());
            pstmt.setString(6, client.getID());
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteData(UsersInRoomModel client) {
        String sql = "DELETE FROM UsersInRoom WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getID());
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public UsersInRoomModel selectData(String id) {

        String sql = "SELECT * FROM UsersInRoom WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UsersInRoomModel user = new UsersInRoomModel(rs.getString("ID"),
                        rs.getString("nickName"),
                        rs.getString("OSName"),
                        rs.getString("clientRole"),
                        rs.getBoolean("isOnline"),
                        rs.getLong("lastAlive")
                );

                return user;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public List<UsersInRoomModel> selectAllData() {
        String sql = "SELECT * FROM UsersInRoom";
        List<UsersInRoomModel> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                UsersInRoomModel user = new UsersInRoomModel(
                        rs.getString("ID"),
                        rs.getString("nickName"),
                        rs.getString("OSName"),
                        rs.getString("clientRole"),
                        rs.getBoolean("isOnline"),
                        rs.getLong("lastAlive")
                );

                users.add(user);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return users;

    }

    @Override
    public void deleteAllData() {
        String sql = "DELETE FROM UsersInRoom";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
