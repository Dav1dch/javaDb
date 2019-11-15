package swing;

import database.Database;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author david
 */
public class MainUi implements TableModelListener {
    JTable jTable1, jTable2;
    public static void main(String[] args) throws SQLException {
        new MainUi();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        jTable1.repaint();
        jTable2.repaint();
        System.out.println(String.valueOf(row) + String.valueOf(col));
    }

    public MainUi() throws SQLException{

        JFrame frame = new JFrame("Database");
        frame.setSize(800, 600);
        frame.setLocation(200, 200);
        frame.setLayout(new GridLayout(3, 2, 10, 5));
        Database db1 = new Database("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        Database db2 = new Database("jdbc:mysql://localhost:3306/test2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        Vector<Vector<String>> table1 = db1.executeSql("select * from table_test");
        Vector<String> name1 = db1.getColoumName();
        jtablemodel tableModel1 = new jtablemodel(name1, table1);
        tableModel1.addTableModelListener(this);
        jTable1 = new JTable(tableModel1);
        Vector<Vector<String>> table2 = db2.executeSql("select * from table_test2");
        Vector<String> name2 = db2.getColoumName();
        jtablemodel tableModel2 = new jtablemodel(name2, table2);
        tableModel2.addTableModelListener(this);
        jTable2 = new JTable(tableModel2);
        frame.add(jTable1);
        frame.add(jTable2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



}
