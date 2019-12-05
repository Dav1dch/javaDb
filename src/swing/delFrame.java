package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

/**
 * @author dav1d
 */
public class delFrame implements ActionListener, ItemListener {
    private final int flag;
    JFrame frame;
    public Table table1;
    public Table table2;
    public MainUi main;
    public JLabel label2;
    JComboBox<String> box;
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    GridBagConstraints gridBagConstraintsRemainder = new GridBagConstraints();
    private JTextField textField;

    public delFrame(Table table1, Table table2, MainUi main, int flag) throws SQLException {
        frame = new JFrame("删除");
        this.table1 = table1;
        this.table2 = table2;
        this.main = main;
        this.flag = flag;
        initFrame();
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initFrame() throws SQLException {

        String[] list = {"数据库1", "数据库2"};
        box = new JComboBox<>(list);
        box.addItemListener(this);
        JPanel panel = new JPanel(gridBagLayout);
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraintsRemainder.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraintsRemainder.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraintsRemainder.weightx = 1;
        gridBagConstraintsRemainder.insets = new Insets(5, 5, 5, 5);
        gridBagLayout.addLayoutComponent(box, gridBagConstraintsRemainder);
        gridBagConstraintsRemainder.weighty = 1;
        JLabel label1 = new JLabel("选择需要删除的数据库");
        label2 = new JLabel("输入所需删除的主键(" + table1.database.getKey(table1.tableModel.tableName).get(0).get(0) + ")的值");
        textField = new JTextField();
        gridBagLayout.addLayoutComponent(label1, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(label2, gridBagConstraintsRemainder);
        gridBagLayout.addLayoutComponent(textField, gridBagConstraintsRemainder);
        panel.add(label1);
        panel.add(box);
        panel.add(label2);
        panel.add(textField);
        frame.setContentPane(panel);
        JButton button = new JButton("确定");
        button.addActionListener(this);
        gridBagLayout.addLayoutComponent(button, gridBagConstraintsRemainder);
        panel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (flag == 1){
            try {
                table1.database.delData(textField.getText(), table1.tableModel.tableName);
                table1.refresh(table1.tableModel.tableName);
                table2.database.delData(textField.getText(), table2.tableModel.tableName);
                table2.refresh(table2.tableModel.tableName);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        if (box.getSelectedItem().equals("数据库1")){
            try {
                table1.database.delData(textField.getText(), table1.tableModel.tableName);
                table1.refresh(table1.tableModel.tableName);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else {
            try {
                table2.database.delData(textField.getText(), table2.tableModel.tableName);
                table2.refresh(table2.tableModel.tableName);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        frame.setVisible(false);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem().toString().equals("数据库1")) {
            try {
                label2.setText("输入所需删除的主键(" + table1.database.getKey(table1.tableModel.tableName).get(0).get(0) + ")的值");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                label2.setText("输入所需删除的主键(" + table2.database.getKey(table2.tableModel.tableName).get(0).get(0) + ")的值");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
}
