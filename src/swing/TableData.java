package swing;

import database.Database;

import java.sql.SQLException;
import java.util.Vector;

public class TableData {
    Vector<Vector<String>> mainData;
    Vector<String> colName;

    TableData(Database dbTable, String tableName) throws SQLException {
        mainData = dbTable.executeSql("select * from " + tableName);
        colName = dbTable.getColoumName();

    }
}
