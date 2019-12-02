package swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;


import database.Database;

/**
 * @author david
 */
public class MainUi {
    private static String prUrl = "jdbc:mysql://localhost:3306/";
    private static String afUrl = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    Database mysql = new Database(prUrl + afUrl);


    public static void main(String[] args) throws SQLException {
        new MainUi();
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
        String dbName1 = mysql.executeSql("show databases;").get(0).get(0);
        String url1 = prUrl + dbName1 + afUrl;
        ComboBox comboBox1 = new ComboBox(url1);
        comboBox1.creatDatabaseBox(mysql);
        ComboBox comboBox2 = new ComboBox(url1);
        comboBox2.creatDatabaseBox(mysql);
        comboBox1.creatTableBox(comboBox1.table.database);
        comboBox2.creatTableBox(comboBox1.table.database);
        gridBagLayout.addLayoutComponent(comboBox1.comboBox, gridBagConstraints);
        gridBagLayout.addLayoutComponent(comboBox2.comboBox, gridBagConstraintsRemainder);
        boxListener(comboBox1);
        boxListener(comboBox2);
        gridBagLayout.addLayoutComponent(comboBox1.comboBox2, gridBagConstraints);
        gridBagLayout.addLayoutComponent(comboBox2.comboBox2, gridBagConstraintsRemainder);
        gridBagConstraints.weighty = 1;
        gridBagConstraintsRemainder.weighty = 1;
        gridBagLayout.addLayoutComponent(comboBox1.table.table, gridBagConstraints);
        gridBagLayout.addLayoutComponent(comboBox2.table.table, gridBagConstraintsRemainder);
        panel.add(comboBox1.comboBox);
        panel.add(comboBox2.comboBox);
        panel.add(comboBox1.comboBox2);
        panel.add(comboBox2.comboBox2);
        panel.add(comboBox1.table.table);
        panel.add(comboBox2.table.table);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void boxListener(ComboBox comboBox) {
        comboBox.comboBox.addItemListener(e -> {
            System.out.println("first");
            if (comboBox.table != null) {
                String dbName1 = e.getItem().toString();
                String url1 = prUrl + dbName1 + afUrl;
                System.out.println("second");
                try {
                    comboBox.table.database = new Database(url1);
                    Vector<Vector<String>> res = comboBox.table.database.executeSql("show tables");
                    String[] resSet = new String[res.size()];
                    for (int i = 0; i < res.size(); i++) {
                        resSet[i] = res.get(i).get(0);
                    }
                    comboBox.comboBox2.setModel(new DefaultComboBoxModel(resSet));
                    comboBox.table.tableData = new TableData(comboBox.table.database, resSet[0]);
                    comboBox.table.tableModel = new jTableModel(comboBox.table.tableData.colName, comboBox.table.tableData.mainData);
                    comboBox.table.table.setModel(comboBox.table.tableModel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {

            }
        });
    }

}


