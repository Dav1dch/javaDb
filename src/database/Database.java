package database;

import java.sql.*;
import java.util.Vector;

/**
 * @author david
 */
public class Database {

    private static ResultSetMetaData metaData;

    private static String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static String url1 = "jdbc:mysql://localhost:3306/test2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    Statement stmt = null;
    ResultSet rs = null;
    public static Vector<Vector<String>> table = null;
    private static ResultSetMetaData RsmetaData = null;

    public static void main(String[] args) throws SQLException {
    }

    public Database(String uUrl) throws SQLException {
        String user = "root";
        String password = "Superzhouqi/15";
        Connection cd = DriverManager.getConnection(uUrl, user, password);
        stmt = cd.createStatement();
        Vector<Vector<String>> table = new Vector<>();
    }

    public  Vector<Vector<String>> executeSql(String sql) throws SQLException {
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSetToVector(rs);
    }

    private static Vector<Vector<String>> resultSetToVector(ResultSet rs) throws SQLException {
        Vector<Vector<String>> table = new Vector<>();
        int j = 0;
        try {
            RsmetaData = rs.getMetaData();
            while (rs.next()) {
                table.add(new Vector<String>());
                for (int i = 1; i <= RsmetaData.getColumnCount(); i++) {
                    table.elementAt(j).add(rs.getString(i));
                }
                j++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;

    }

    public Vector<String> getColoumName() throws SQLException {
        Vector<String> name = new Vector<>();
        try {
            RsmetaData = rs.getMetaData();
            int num = RsmetaData.getColumnCount();
            for (int i = 1; i < num + 1; i++) {
                name.add(RsmetaData.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }


}
