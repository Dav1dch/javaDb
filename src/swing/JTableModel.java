package swing;

import database.Database;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

/**
 * @author David
 */
public class JTableModel extends DefaultTableModel {

    String[][] table;
    String[] colName;
    Database database;
    String tableName;
    MainUi main;
    int flag;

    public JTableModel(TableData tableData, Database db, MainUi mainUi, int flag) {
        super(tableData.getData(), tableData.getColName());
        this.table = tableData.getData();
        this.colName = tableData.getColName();
        this.tableName = tableData.getTableName();
        this.database = db;
        this.main = mainUi;
        this.flag = flag;
    }


    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        return table[rowIndex][columnIndex];
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        table[rowIndex][columnIndex] = (String) aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }


}
