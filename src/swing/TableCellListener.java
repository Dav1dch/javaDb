package swing;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

/**
 * @author dav1d
 */

public class TableCellListener implements PropertyChangeListener, Runnable
{
    private JTable table;
    private Table wholeTable;
    private Action action;

    private int row;
    private int column;
    private Object oldValue;
    private Object newValue;

    /**
     *  Create a TableCellListener.
     *
     *  @param table   the table to be monitored for data changes
     *  @param action  the Action to invoke when cell data is changed
     */

    public TableCellListener(Table table, Action action)
    {
        this.wholeTable = table;
        this.table = table.table;
        this.action = action;
        this.table.addPropertyChangeListener( this );
    }

    /**
     *  Create a TableCellListener with a copy of all the data relevant to
     *  the change of data for a given cell.
     *
     *  @param row  the row of the changed cell
     *  @param column  the column of the changed cell
     *  @param oldValue  the old data of the changed cell
     *  @param newValue  the new data of the changed cell
     */
    private TableCellListener(Table wholeTable,JTable table, int row, int column, Object oldValue, Object newValue)
    {
        this.wholeTable = wholeTable;
        this.table = table;
        this.row = row;
        this.column = column;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     *  Get the column that was last edited
     *
     *  @return the column that was edited
     */
    public int getColumn()
    {
        return column;
    }

    public Table getWholeTable(){
        return wholeTable;
    }

    /**
     *  Get the new value in the cell
     *
     *  @return the new value in the cell
     */
    public Object getNewValue()
    {
        return newValue;
    }

    /**
     *  Get the old value of the cell
     *
     *  @return the old value of the cell
     */
    public Object getOldValue()
    {
        return oldValue;
    }

    /**
     *  Get the row that was last edited
     *
     *  @return the row that was edited
     */
    public int getRow()
    {
        return row;
    }

    /**
     *  Get the table of the cell that was changed
     *
     *  @return the table of the cell that was changed
     */
    public JTable getTable()
    {
        return table;
    }
    //
//  Implement the PropertyChangeListener interface
//
    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        //  A cell has started/stopped editing

        if ("tableCellEditor".equals(e.getPropertyName()))
        {
            if (table.isEditing()){
                //System.out.printf("tableCellEditor is editing..%n");
                processEditingStarted();
            }
            else{
                //System.out.printf("tableCellEditor editing stopped..%n");
                processEditingStopped();
            }

        }
    }

    /*
     *  Save information of the cell about to be edited
     */
    private void processEditingStarted()
    {
        //  The invokeLater is necessary because the editing row and editing
        //  column of the table have not been set when the "tableCellEditor"
        //  PropertyChangeEvent is fired.
        //  This results in the "run" method being invoked

        SwingUtilities.invokeLater( this );
    }
    /*
     *  See above.
     */
    @Override
    public void run()
    {
        row = table.convertRowIndexToModel( table.getEditingRow() );
        column = table.convertColumnIndexToModel( table.getEditingColumn() );
        oldValue = table.getModel().getValueAt(row, column);
        //这里应对oldValue为null的情况做处理，否则将导致原值与新值均为空时仍被视为值改变
        if(oldValue == null) {
            oldValue = "";
        }
        newValue = null;
    }

    /*
     *    Update the Cell history when necessary
     */
    private void processEditingStopped()
    {
        newValue = table.getModel().getValueAt(row, column);
        //这里应对newValue为null的情况做处理，否则后面会抛出异常
        if(newValue == null) {
            newValue = "";
        }
        //  The data has changed, invoke the supplied Action
        if (! newValue.equals(oldValue))
        {
            //  Make a copy of the data in case another cell starts editing
            //  while processing this change


            TableCellListener tcl = new TableCellListener(
                    getWholeTable(), getTable(), getRow(), getColumn(), getOldValue(), getNewValue());

            ActionEvent event = new ActionEvent(
                    tcl,
                    ActionEvent.ACTION_PERFORMED,
                    "");
            action.actionPerformed(event);
        }
    }
}