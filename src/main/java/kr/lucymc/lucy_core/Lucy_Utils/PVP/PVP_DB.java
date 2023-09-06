package kr.lucymc.lucy_core.Lucy_Utils.PVP;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import static kr.lucymc.lucy_core.Lucy_Core.connection;


public class PVP_DB {
    public static void PVPInsert(UUID userid, int F) {
        String sql = "insert into pvp (UserID, IO) values ('" + userid +"','" + F +"');";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void PVPUpdate(UUID userid, int F) {
        String sql = "update pvp set IO='"+F+"' where UserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Integer PVPSelect(UUID userid) {
        String sql = "select * from pvp where UserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while(true){
                try {
                    if (!Objects.requireNonNull(rs).next()) break;
                    count = rs.getInt("IO");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rs.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isDataExists(String tableName, String columnName, String value) {
        boolean exists = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, value);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = (count > 0);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return exists;
    }
}
