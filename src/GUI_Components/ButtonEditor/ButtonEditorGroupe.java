package GUI_Components.ButtonEditor;

import GUI.GUI_Cours;

import javax.swing.*;

public class ButtonEditorGroupe extends ButtonEditor {

    private GUI_Cours gui;

    public ButtonEditorGroupe(JCheckBox checkBox, GUI_Cours gui) {
        super(checkBox);
        this.gui = gui;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            gui.deleteGroupe(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }
}
