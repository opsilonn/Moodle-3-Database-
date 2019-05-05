package GUI_Components;

import GUI.GUI_Cours;
import GUI.GUI_chercherPersonne;
import GUI_Components.ButtonEditor.ButtonEditor;

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
            JOptionPane.showMessageDialog(button, "Le prof n'enseigne plus ce cours.");
            /*CHOISIR L'ACTION*/
            gui.deleteProf(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }
}
