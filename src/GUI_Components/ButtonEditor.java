package GUI_Components;

import GUI.GUI_chercherPersonne;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {

    private GUI_chercherPersonne gui;

    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox, GUI_chercherPersonne gui) {
        super(checkBox);
        this.gui = gui;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
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
    public Object getCellEditorValue() {
        if (isPushed) {
            JOptionPane.showMessageDialog(button, "L'adresse à été supprimé.");
            gui.deleteAddress(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
