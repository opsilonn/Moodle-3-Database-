package GUI_Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonFunctions {

    public static class JTableButtonRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton) value;
            return button;
        }
    }

    public static class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the column of the button
            int row = e.getY() / table.getRowHeight(); //get the row of the button

            /*Checking the row or column is valid or not*/
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    /*perform a click event*/
                    ((JButton) value).doClick();
                }
            }
        }
    }
}
