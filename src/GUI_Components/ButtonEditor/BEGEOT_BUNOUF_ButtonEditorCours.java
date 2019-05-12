package GUI_Components.ButtonEditor;

import GUI.BEGEOT_BUNOUF_GUI_chercherPersonne;

import javax.swing.*;

public class BEGEOT_BUNOUF_ButtonEditorCours extends BEGEOT_BUNOUF_ButtonEditor {

    private BEGEOT_BUNOUF_GUI_chercherPersonne gui;

    public BEGEOT_BUNOUF_ButtonEditorCours(JCheckBox checkBox, BEGEOT_BUNOUF_GUI_chercherPersonne gui) {
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
