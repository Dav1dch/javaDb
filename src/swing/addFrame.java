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
public class addFrame implements ItemListener, ActionListener {
    JFrame frame;
    Table table1;
    Table table2;
    JLabel[] labels;
    JTextField[] textFields;
    JPanel textPanel;
    JComboBox<String> box;
    JScrollPane scrollPane;
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    GridBagConstraints gridBagConstraintsRemainder = new GridBagConstraints();
    MainUi main;
    int size;

    public addFrame(Table table1, Table table2, MainUi main) {
        this.main = main;
        this.table1 = table1;
        this.table2 = table2;
        frame = new JFrame("增加");
        initFrame();
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void initFrame() {
        String[] list = {"数据库1", "数据库2"};
        box = new JComboBox<>(list);
        box.addItemListener(this);
        JPanel panel = new JPanel(gridBagLayout);
        textPanel = new JPanel(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraintsRemainder.fill = GridBagConstraints.BOTH;
        gridBagConstraintsRemainder.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraintsRemainder.weightx = 1;
        gridBagConstraintsRemainder.insets = new Insets(5, 5, 5, 5);
        gridBagLayout.addLayoutComponent(box, gridBagConstraintsRemainder);
        gridBagConstraintsRemainder.weighty = 1;
        gridBagLayout.addLayoutComponent(textPanel, gridBagConstraintsRemainder);
        panel.add(box);
        frame.setContentPane(panel);
        creatPanel(table1);
        scrollPane = new JScrollPane(textPanel);
        gridBagLayout.addLayoutComponent(scrollPane, gridBagConstraintsRemainder);
        panel.add(scrollPane);
        JButton button = new JButton("确定");
        button.addActionListener(this);
        gridBagLayout.addLayoutComponent(button, gridBagConstraintsRemainder);
        panel.add(button);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object item = e.getItem();
        if ("数据库1".equals(item)) {
            creatPanel(table1);
        } else if ("数据库2".equals(item)) {
            creatPanel(table2);
        }

    }


    public void creatPanel(Table table) {
        size = table.tableData.colName.size();
        labels = new JLabel[size];
        textFields = new JTextField[size];
        textPanel.removeAll();
        for (int i = 0; i < size; i++) {
            labels[i] = new JLabel(table.tableData.colName.get(i));
            textFields[i] = new JTextField();
            gridBagLayout.addLayoutComponent(labels[i], gridBagConstraints);
            gridBagLayout.addLayoutComponent(textFields[i], gridBagConstraintsRemainder);
            textPanel.add(labels[i]);
            textPanel.add(textFields[i]);
        }
        textPanel.repaint();
        textPanel.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] values = new String[size];
        String[] colNames = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = textFields[i].getText();
            colNames[i] = labels[i].getText();
        }
        try {

            if (box.getSelectedItem().toString().equals("数据库1")) {
                table1.database.insertValue(table1.tableModel.tableName, colNames, values);
                table1.refresh(table1.tableModel.tableName);
            } else {
                table2.database.insertValue(table2.tableModel.tableName, colNames, values);
                table2.refresh(table2.tableModel.tableName);
            }
            main.sync();
        }catch (SQLException e1){
            System.err.println(e1);
        }
        frame.setVisible(false);

    }
}
