/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeko.swing.tablas.editores;

import com.codeko.util.Str;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

/**
 * Copyright Codeko Informática 2008
 * @author Codeko
 */
public class CdkScrollTextEditor extends AbstractCellEditor implements TableCellEditor {

    JTextArea ta = new JTextArea();
    JScrollPane scroll = new JScrollPane(ta);

    public CdkScrollTextEditor() {
        super();
        ta.setFont(new JLabel().getFont());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        ta.setText(Str.noNulo(value));
        return scroll;
    }

    public Object getCellEditorValue() {
        return ta.getText();
    }
}