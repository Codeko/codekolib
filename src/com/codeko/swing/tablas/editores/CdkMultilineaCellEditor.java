package com.codeko.swing.tablas.editores;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

public class CdkMultilineaCellEditor extends CdkDualCellEditor {

    public CdkMultilineaCellEditor(TableCellEditor editor) {
        super(editor);
    }

    protected void editCell(JTable table, Object partialValue, int row, int column) {
        JTextArea textArea = new JTextArea(10, 50);
        if (partialValue != null) {
            textArea.setText((String) partialValue);
            textArea.setCaretPosition(0);
        }
        int result = JOptionPane.showOptionDialog(table, new JScrollPane(textArea), table.getColumnName(column), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            table.setValueAt(textArea.getText(), row, column);
        }
    }
} 