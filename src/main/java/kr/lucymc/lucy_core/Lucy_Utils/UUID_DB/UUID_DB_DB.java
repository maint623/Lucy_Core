package kr.lucymc.lucy_core.Lucy_Utils.UUID_DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import static kr.lucymc.lucy_core.Lucy_Core.connection;

public class UUID_DB_DB {
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

    public static void PBInsert(UUID userid, String Prefix) {
        String sql = "insert into userlist (UserID, UserName) values ('" + userid +"','" + Prefix +"');";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void PBUpdate(UUID userid, String Prefix) {
        String sql = "update userlist set UserName='"+Prefix+"' where UserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String PBSelect(UUID userid) {
        String sql = "select * from userlist where UserID='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            String name = null;
            while(true){
                try {
                    if (!Objects.requireNonNull(rs).next()) break;
                    name = rs.getString("UserName");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rs.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
