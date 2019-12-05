package swing;

import database.Database;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author dav1d
 */
public class ComboBox {
    JComboBox<String> comboBox;
    JComboBox<String> comboBox2;
    Database database;
    MainUi main;
    int flag;

    public ComboBox(MainUi mainUi,Database database) throws SQLException {
        this.main = mainUi;
        this.database = database;
        creatDatabaseBox(database);
        String dbName = database.showDaetabases().get(0).get(0);
        database.useDatabase(dbName);
        creatTableBox(database);

    }

    public void setFlag(int f){
        flag = f;
    }


    public void creatTableBox(Database db) throws SQLException {
        this.comboBox2 = buildComboBox(db.showTables());
    }

    public void creatDatabaseBox(Database db) throws SQLException {
        this.comboBox = buildComboBox(db.showDaetabases());
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
