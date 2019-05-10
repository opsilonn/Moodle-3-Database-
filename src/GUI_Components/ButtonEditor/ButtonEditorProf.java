package GUI_Components.ButtonEditor;

import GUI.GUI_Cours;

import javax.swing.*;

public class ButtonEditorProf extends ButtonEditor {

    private GUI_Cours gui;

    public ButtonEditorProf(JCheckBox checkBox, GUI_Cours gui) {
        super(checkBox);
        this.gui = gui;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            gui.deleteProf(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }
}
