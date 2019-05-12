package GUI_Components.ButtonEditor;

import GUI.BEGEOT_BUNOUF_GUI_Cours;

import javax.swing.*;

public class BEGEOT_BUNOUF_ButtonEditorProf extends BEGEOT_BUNOUF_ButtonEditor {

    private BEGEOT_BUNOUF_GUI_Cours gui;

    public BEGEOT_BUNOUF_ButtonEditorProf(JCheckBox checkBox, BEGEOT_BUNOUF_GUI_Cours gui) {
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
