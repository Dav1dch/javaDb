package swing;

import database.Database;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.SQLException;

/**
 * @author dav1d
 */
public class Table implements TableModelListener {
    JTable table;
    Database database;
    TableModel tableModel;
    TableData tableData;

    public Table(Database db) throws SQLException {
        this.table = new JTable();
        this.database = db;
        this.tableData = new TableData(this.database, this.database.executeSql("show tables").get(0).get(0));
        this.tableModel = new jTableModel(this.tableData.colName, this.tableData.mainData);

    }

    public void creatTable(String tableName) throws SQLException {
        this.tableData = new TableData(this.database, tableName);
        this.tableModel = new jTableModel(this.tableData.colName, this.tableData.mainData);
        this.tableModel.addTableModelListener(this);
        this.table = new JTable(this.tableModel);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        table.repaint();
    }
}
