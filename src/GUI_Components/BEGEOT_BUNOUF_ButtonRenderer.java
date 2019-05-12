package GUI_Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BEGEOT_BUNOUF_ButtonRenderer extends JButton implements TableCellRenderer {

    public BEGEOT_BUNOUF_ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText("X");
        return this;
    }
}
