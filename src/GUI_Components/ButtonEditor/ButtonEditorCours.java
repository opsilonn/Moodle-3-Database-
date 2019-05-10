package GUI_Components.ButtonEditor;

import GUI.GUI_chercherPersonne;

import javax.swing.*;

public class ButtonEditorCours extends ButtonEditor {

    private GUI_chercherPersonne gui;

    public ButtonEditorCours(JCheckBox checkBox, GUI_chercherPersonne gui) {
        super(checkBox);
        this.gui = gui;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            gui.deleteCours(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }
}
