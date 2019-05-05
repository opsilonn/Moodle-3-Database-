package GUI_Components.ButtonEditor;

import GUI.GUI_Groupe;

import javax.swing.*;

public class ButtonEditorCours4Groupe extends ButtonEditor {

    private GUI_Groupe gui;

    public ButtonEditorCours4Groupe(JCheckBox checkBox, GUI_Groupe gui) {
        super(checkBox);
        this.gui = gui;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            JOptionPane.showMessageDialog(button, "Ce groupe ne suit plus ce cours.");
            /*CHOISIR L'ACTION*/
            gui.deleteCours(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }
}
