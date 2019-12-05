package swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;

/**
 * @author david
 */
public class MainUi implements ActionListener {
    Table table1;
    Table table2;
    JPanel checkBoxPanel = new JPanel();
    JPanel panel;
    GridBagLayout gridBagLayout;
    ArrayList<String> commonList = new ArrayList<>();
    ArrayList<JCheckBox> boxes = new ArrayList<>();

    public static void main(String[] args) throws SQLException {
        new MainUi();
    }

    public MainUi() throws SQLException {
        table1 = new Table(this);
        table2 = new Table(this);
        table1.setFlag(1);
        table2.setFlag(0);
        tableListener();

        JFrame frame = new JFrame("Database");
        gridBagLayout = new GridBagLayout();
        frame.setSize(1024, 1024);
        frame.setLocationRelativeTo(null);
        panel = new JPanel(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraintsRemainder = new GridBagConstraints();
        JLabel label1 = new JLabel("数据库1");
        JLabel label2 = new JLabel("数据库2");
        JLabel label3 = new JLabel("选择需要同步的共有字段");
        JLabel label4 = new JLabel("数据库1");
        JLabel label5 = new JLabel("数据库2");
        JButton button1 = new JButton("增加");
        JButton button2 = new JButton("删除");
        button1.addActionListener(this);
        button2.addActionListener(this);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraintsRemainder.fill = GridBagConstraints.BOTH;
        gridBagConstraintsRemainder.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraintsRemainder.weightx = 1;
        gridBagConstraintsRemainder.insets = new Insets(5, 5, 5, 5);
        creatCheckBoxes();
        gridBagLayout.addLayoutComponent(label2, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(label1, gridBagConstraints);
        gridBagLayout.addLayoutComponent(label3, gridBagConstraints);
        gridBagLayout.addLayoutComponent(label5, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(label4, gridBagConstraints);
        gridBagLayout.addLayoutComponent(button1, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(button2, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(checkBoxPanel, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(table1.comboBox.comboBox, gridBagConstraints);
        gridBagLayout.addLayoutComponent(table2.comboBox.comboBox, gridBagConstraintsRemainder);
        boxListener(table1);
        boxListener(table2);
        gridBagLayout.addLayoutComponent(table1.comboBox.comboBox2, gridBagConstraints);
        gridBagLayout.addLayoutComponent(table2.comboBox.comboBox2, gridBagConstraintsRemainder);
        gridBagConstraints.weighty = 1;
        gridBagConstraintsRemainder.weighty = 1;
        JScrollPane jScrollPane1 = new JScrollPane(table1.table);
        JScrollPane jScrollPane2 = new JScrollPane(table2.table);
        gridBagLayout.addLayoutComponent(jScrollPane1, gridBagConstraints);
        gridBagLayout.addLayoutComponent(jScrollPane2, gridBagConstraintsRemainder);
        panel.add(label1);
        panel.add(label2);
        panel.add(table1.comboBox.comboBox);
        panel.add(table2.comboBox.comboBox);
        panel.add(table1.comboBox.comboBox2);
        panel.add(table2.comboBox.comboBox2);
        panel.add(label3);
        panel.add(checkBoxPanel);
        panel.add(button1);
        panel.add(button2);
        panel.add(label4);
        panel.add(label5);
        panel.add(jScrollPane1);
        panel.add(jScrollPane2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void tableListener() {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                String pkValue = tcl.getWholeTable().tableModel.getValueAt(tcl.getRow(), 0);
                try {
                    String pk = tcl.getWholeTable().database.getKey(tcl.getWholeTable().tableModel.tableName).get(0).get(0);
                    ArrayList<String> colList = new ArrayList<>();
                    Collections.addAll(colList, tcl.getWholeTable().tableModel.colName);
                    if (colList.indexOf(pk) == tcl.getColumn()) {
                        tcl.getWholeTable().database.updateData(pk, (String) tcl.getOldValue(), pk, (String) tcl.getNewValue(), tcl.getWholeTable().tableModel.tableName);
                        syncUpdateDatabase(pkValue, tcl.getWholeTable().flag, (String) tcl.getNewValue(),
                                tcl.getWholeTable().tableModel.colName[tcl.getColumn()], true, (String) tcl.getOldValue());
                    } else {
                        syncUpdateDatabase(pkValue, tcl.getWholeTable().flag, (String) tcl.getNewValue(),
                                tcl.getWholeTable().tableModel.colName[tcl.getColumn()], false, (String) tcl.getOldValue());

                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };

        TableCellListener tcl = new TableCellListener(table1, action);
        TableCellListener tcl1 = new TableCellListener(table2, action);
    }

    private void creatCheckBoxes() {
        String[] list1 = table1.tableData.getColName();
        String[] list2 = table2.tableData.getColName();
        ArrayList<String> tmp = new ArrayList<>();
        Collections.addAll(tmp, list2);
        commonList.clear();
        for (String name :
                list1) {
            if (tmp.contains(name)) {
                commonList.add(name);
            }
        }
        boxes.clear();
        checkBoxPanel.removeAll();
        for (int i = 0; i < commonList.size(); i++) {
            boxes.add(new JCheckBox(commonList.get(i)));
            checkBoxPanel.add(boxes.get(i));
        }
        checkBoxPanel.repaint();
        checkBoxPanel.revalidate();

    }

    public void syncUpdateDatabase(String pkValue, int dbFlag, String target, String colName, boolean b, String oldValue) throws SQLException {
        String tableName1 = table1.tableModel.tableName;
        String tableName2 = table2.tableModel.tableName;
        String pk1 = table1.database.getKey(tableName1).get(0).get(0);
        String pk2 = table2.database.getKey(tableName2).get(0).get(0);
        if (b) {
            pkValue = oldValue;
        }
        if (pk1.equals(pk2)) {
            if (commonList.contains(colName)) {
                if (boxes.get(commonList.indexOf(colName)).isSelected()) {
                    table1.database.updateData(pk1, pkValue, colName, target, tableName1);
                    table2.database.updateData(pk2, pkValue, colName, target, tableName2);
                    table1.refresh(tableName1);
                    table2.refresh(tableName2);
                } else if (dbFlag == 1) {
                    table1.database.updateData(pk1, pkValue, colName, target, tableName1);
                    table1.refresh(tableName1);
                } else if (dbFlag == 0) {
                    table2.database.updateData(pk2, pkValue, colName, target, tableName2);
                    table2.refresh(tableName2);
                }
            }
        }


    }

    public void boxListener(Table table) {
        table.comboBox.comboBox.addItemListener(e -> {
            String dbName1 = e.getItem().toString();
            try {
                table.database.useDatabase(dbName1);
                Vector<Vector<String>> res = table.database.showTables();
                String[] resSet = new String[res.size()];
                for (int i = 0; i < res.size(); i++) {
                    resSet[i] = res.get(i).get(0);
                }
                table.comboBox.comboBox2.setModel(new DefaultComboBoxModel<>(resSet));
                table.refresh(resSet[0]);
                creatCheckBoxes();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        table.comboBox.comboBox2.addItemListener(e -> {
            try {
                table.refresh(e.getItem().toString());
                creatCheckBoxes();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "增加":
                new addFrame(table1, table2, this);
                break;
            case "删除":
                String tableName1 = table1.tableModel.tableName;
                String tableName2 = table2.tableModel.tableName;
                int flag = 0;
                try {
                    String pk2 = table2.database.getKey(tableName2).get(0).get(0);
                    String pk1 = table1.database.getKey(tableName1).get(0).get(0);
                    if (pk1.equals(pk2)){
                        for (JCheckBox box :
                                boxes) {
                            if (box.getText().equals(pk1) && box.isSelected()){
                                flag = 1;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    new delFrame(table1, table2, this, flag);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                return;
        }
    }


    public void sync() throws SQLException {
        String pk1 = table1.database.getKey(table1.tableModel.tableName).get(0).get(0);
        String pk2 = table2.database.getKey(table2.tableModel.tableName).get(0).get(0);
        if (pk1.equals(pk2)) {
            ArrayList<String> colNames = new ArrayList<>();
            for (JCheckBox box :
                    boxes) {
                if (box.isSelected()) {
                    colNames.add(box.getText());
                }
            }
            if (colNames.size() > 0) {
                table2.database.sync(table1, colNames, table2.tableModel.tableName);
                table1.database.sync(table2, colNames, table1.tableModel.tableName);
                table1.refresh(table1.tableModel.tableName);
                table2.refresh(table2.tableModel.tableName);
            }
        }
    }
}


