package GUI_Components.ButtonEditor;

import GUI.BEGEOT_BUNOUF_GUI_Groupe;

import javax.swing.*;

public class BEGEOT_BUNOUF_ButtonEditorCours4Groupe extends BEGEOT_BUNOUF_ButtonEditor {

    private BEGEOT_BUNOUF_GUI_Groupe gui;

    public BEGEOT_BUNOUF_ButtonEditorCours4Groupe(JCheckBox checkBox, BEGEOT_BUNOUF_GUI_Groupe gui) {
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
