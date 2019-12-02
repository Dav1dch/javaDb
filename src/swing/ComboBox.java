package swing;

import database.Database;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author dav1d
 */
public class ComboBox {
    JComboBox<String> comboBox;
    JComboBox<String> comboBox2;
    Table table;
    Database database;
    private static String prUrl = "jdbc:mysql://localhost:3306/";
    private static String afUrl = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public ComboBox(String url) throws SQLException {
        database = new Database(url);
        creatDatabaseBox(database);
        String tableName = database.executeSql("show tables").get(0).get(0);
        comboBox = new JComboBox<>();
        table = new Table(database);
        creatTableBox(database);

    }


    public void creatTableBox(Database db) throws SQLException {
        this.comboBox2 = buildComboBox(db.executeSql("show tables;"));
    }

    public void creatDatabaseBox(Database db) throws SQLException {
        this.comboBox = buildComboBox(db.executeSql("show databases;"));
    }

    public JComboBox<String> buildComboBox(Vector<Vector<String>> nameList) {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (Vector<String> name :
                nameList) {
            comboBoxModel.addElement(name.get(0));
        }
        return new JComboBox<String>(comboBoxModel);
    }


}
