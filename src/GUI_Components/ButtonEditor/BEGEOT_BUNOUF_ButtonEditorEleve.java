package GUI_Components.ButtonEditor;

import GUI.BEGEOT_BUNOUF_GUI_Groupe;

import javax.swing.*;

public class BEGEOT_BUNOUF_ButtonEditorEleve extends BEGEOT_BUNOUF_ButtonEditor {

    private BEGEOT_BUNOUF_GUI_Groupe gui;

    public BEGEOT_BUNOUF_ButtonEditorEleve(JCheckBox checkBox, BEGEOT_BUNOUF_GUI_Groupe gui) {
        super(checkBox);
        this.gui = gui;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            gui.deleteEleves(Integer.parseInt(label));
        }
        isPushed = false;
        return label;
    }
}
