package database;

import swing.Table;

import java.sql.*;
import java.util.ArrayList;
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
    private static ResultSetMetaData RsmetaData = null;

    public static void main(String[] args) {
    }

    public Database(String uUrl) throws SQLException {
        String user = "root";
        String password = "Superzhouqi/15";
        Connection cd = DriverManager.getConnection(uUrl, user, password);
        stmt = cd.createStatement();
        Vector<Vector<String>> table = new Vector<>();
    }

    public Vector<Vector<String>> executeQuerySql(String sql) throws SQLException {
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSetToVector(rs);
    }

    public void executeSql(String sql) throws SQLException {
        stmt.execute(sql);
    }

    public void useDatabase(String dbName) throws SQLException {
        stmt.execute("use " + dbName + ";");
    }

    public Vector<Vector<String>> showDaetabases() throws SQLException {
        return this.executeQuerySql("show databases;");
    }

    public Vector<Vector<String>> showTables() throws SQLException {
        return this.executeQuerySql("show tables;");
    }

    public Vector<Vector<String>> selectAll(String tbName) throws SQLException {
        return this.executeQuerySql("select * from " + tbName + ";");
    }

    public Vector<Vector<String>> getKey(String tbName) throws SQLException {
        return this.executeQuerySql("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='"
                + tbName + "';");
    }

    private static Vector<Vector<String>> resultSetToVector(ResultSet rs) {
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

    public Vector<String> getColumnName() {
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


    public void updateData(String pk, String pkValue, String colName, String target, String tableName) throws SQLException {
        this.executeSql("update " + tableName + " set " + colName + " = '" + target + "' where " + pk + " = " + pkValue + ";");
    }

    public void insertValue(String tableName, String[] colNames, String[] values) throws SQLException {
        String sql = "insert into " + tableName + " (";
        for (int i = 0; i < colNames.length; i++) {
            if (i != colNames.length - 1) {
                sql += colNames[i] + ", ";
            } else {
                sql += colNames[i] + ") ";
            }
        }
        sql += "values (";
        for (int i = 0; i < values.length; i++) {
            if (i != values.length - 1) {
                sql += "'" + values[i] + "', ";
            } else {
                sql += "'" + values[i] + "') ";
            }
        }
        sql += ";";
        this.executeSql(sql);
    }


    public void sync(Table table, ArrayList<String> colNames, String tableName) throws SQLException {
        String tbName = table.tableModel.tableName;
        String sql = "update " + tableName + ", " + table.comboBox.comboBox.getSelectedItem().toString() + "." + tbName + " set ";
        System.out.println(colNames.toString());
        for (int i = 0; i < colNames.size(); i++) {
            if (i != colNames.size() - 1) {
                sql += tableName + "." + colNames.get(i) + "=" + tbName + "." + colNames.get(i) + ",";
            } else {
                sql += tableName + "." + colNames.get(i) + "=" + tbName + "." + colNames.get(i);
            }
        }
        sql += " where ";
        sql += tableName + "." + this.getKey(tableName).get(0).get(0) + "=" + tbName + "."
                + table.database.getKey(tbName).get(0).get(0) + ";";
        System.out.println(sql);
        this.executeSql(sql);

    }

    public void delData(String text, String tableName) throws SQLException {
        String sql = "delete from " + tableName + " where " + tableName + "." + this.getKey(tableName).get(0).get(0)
                + "=" + text + ";";
        System.out.println(sql);
        this.executeSql(sql);
    }
}
