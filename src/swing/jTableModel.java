package swing;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * @author David
 */
public class jTableModel extends AbstractTableModel {

    Vector<Vector<String>> table;
    Vector<String> colName;

    jTableModel(Vector<String> daColName, Vector<Vector<String>> dbTable){
        colName = daColName;
        table = dbTable;

    }
    @Override
    public int getRowCount() {
        return table.size();
    }

    @Override
    public int getColumnCount() {
        return colName.size();
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        return table.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
       table.get(rowIndex).set(columnIndex, (String)aValue);
       fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
       return true;
    }
}
