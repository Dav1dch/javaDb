package swing;

import database.Database;

import java.sql.SQLException;
import java.util.Vector;

/**
 * @author dav1d
 */
public class TableData {
    Vector<Vector<String>> mainData;
    Vector<String> colName;
    private String tableName;

    TableData(Database dbTable, String tableName) throws SQLException {
        this.setTableName(tableName);
        mainData = dbTable.selectAll(tableName);
        colName = dbTable.getColumnName();

    }

    public String[] getColName() {
        String[] res = new String[colName.size()];
        for (int i = 0; i < colName.size(); i++) {
            res[i] = colName.get(i);
        }
        return res;
    }

    public String[][] getData() {
        String[][] res = new String[mainData.size()][colName.size()];
        for (int i = 0; i < mainData.size(); i++) {
            for (int j = 0; j < colName.size(); j++) {
                res[i][j] = mainData.get(i).get(j);
            }
        }
        return res;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
