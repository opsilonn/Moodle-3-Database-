package GUI_Components.ButtonEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class BEGEOT_BUNOUF_ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    protected String label;
    protected boolean isPushed;
    private ActionListener LISTEN_TO_BUTTON;

    public BEGEOT_BUNOUF_ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        if (checkBox == null)
            return;


        LISTEN_TO_BUTTON = e -> fireEditingStopped();

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(LISTEN_TO_BUTTON);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText("X");
        isPushed = true;
        return button;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    public void deleteActionListener() {
        button.removeActionListener(LISTEN_TO_BUTTON);
    }
}
