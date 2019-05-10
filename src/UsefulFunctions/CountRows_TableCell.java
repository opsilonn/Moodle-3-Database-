package UsefulFunctions;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.ResultSet;

public class CountRows_TableCell
{

    public static int getRows(ResultSet res)
    {
        int totalRows = 0;
        try
        {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            return 0;
        }
        return totalRows;
    }

    public static TableModel createModel(Object[][] objects, String[] columns) {
        return new DefaultTableModel(objects, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1) {
                    return true;
                }
                return false;
            }
        };
    }
}
