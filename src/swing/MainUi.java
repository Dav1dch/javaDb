package swing;

import database.Database;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    }

    public MainUi() throws SQLException {

        JFrame frame = new JFrame("Database");
        GridBagLayout gridBagLayout = new GridBagLayout();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        GridBagConstraints gridBagConstraintsRemainder = new GridBagConstraints();
        gridBagConstraintsRemainder.fill = GridBagConstraints.BOTH;
        gridBagConstraintsRemainder.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraintsRemainder.weightx = 1;
        gridBagConstraintsRemainder.insets = new Insets(5, 5, 5, 5);
        Database db1 = new Database("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        Database db2 = new Database("jdbc:mysql://localhost:3306/test2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        Database selectDatabase = new Database("jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        JComboBox<String> comboBox1 = buildComboBox(selectDatabase.executeSql("show databases;").toArray());
        JComboBox<String> comboBox2 = buildComboBox(selectDatabase.executeSql("show databases;").toArray());
        gridBagLayout.addLayoutComponent(comboBox1, gridBagConstraints);
        gridBagLayout.addLayoutComponent(comboBox2, gridBagConstraintsRemainder);
        TableData tableOne = new TableData(db1, "test");
        TableData tableTwo = new TableData(db2, "table_test2");
        jTableModel tableModel1 = new jTableModel(tableOne.colName, tableOne.mainData);
        jTableModel tableModel2 = new jTableModel(tableTwo.colName, tableTwo.mainData);
        tableModel1.addTableModelListener(this);
        tableModel2.addTableModelListener(this);
        jTable1 = new JTable(tableModel1);
        jTable2 = new JTable(tableModel2);
        gridBagConstraints.weighty = 1;
        gridBagConstraintsRemainder.weighty = 1;
        gridBagLayout.addLayoutComponent(jTable1, gridBagConstraints);
        gridBagLayout.addLayoutComponent(jTable2, gridBagConstraintsRemainder);
        panel.add(comboBox1);
        panel.add(comboBox2);
        panel.add(jTable1);
        panel.add(jTable2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JComboBox<String> buildComboBox(Object[] nameList) {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (Object name :
                nameList) {
            String substring = name.toString().substring(1, name.toString().length() - 1);
            comboBoxModel.addElement(substring);
        }
        JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    System.out.println(e.toString());
                    System.out.println(e.getItem());
                }
            }
        });
        return new JComboBox<String>(comboBoxModel);
    }

    static class TableData {
        Vector<Vector<String>> mainData;
        Vector<String> colName;

        TableData(Database dbTable, String tableName) throws SQLException {
            mainData = dbTable.executeSql("select * from " + tableName);
            colName = dbTable.getColoumName();

        }
    }


}
