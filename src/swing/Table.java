package swing;

import database.Database;

import javax.swing.*;
import java.sql.SQLException;

/**
 * @author dav1d
 */
public class Table {
    JTable table;
    Database database;
    JTableModel tableModel;
    TableData tableData;
    ComboBox comboBox;
    private static String prUrl = "jdbc:mysql://localhost:3306/";
    private static String afUrl = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    MainUi main;
    int flag;

    public Table(MainUi main) throws SQLException {
        this.main = main;
        database = new Database(prUrl + afUrl);
        comboBox = new ComboBox(main, database);
        String tmp = database.showDaetabases().get(0).get(0);
        database = new Database(prUrl + tmp + afUrl);
        this.tableData = new TableData(this.database, this.database.showTables().get(0).get(0));
        this.tableModel = new JTableModel(this.tableData, database, main, flag);
        this.table = new JTable(tableModel);
    }

    public void setFlag(int f){
        flag = f;
        tableModel.flag = f;
        comboBox.setFlag(f);
    }

}
