package com.codeko.swing.tablas.editores;

import java.awt.BorderLayout;
import java.awt.Component;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public abstract class CdkDualCellEditor implements TableCellEditor, ActionListener {

    private TableCellEditor editor;
    private JButton customEditorButton = new JButton("...");
    protected JTable table;
    protected int row,  column;

    public CdkDualCellEditor(TableCellEditor editor) {
        this.editor = editor;
        customEditorButton.addActionListener(this);
        customEditorButton.setFocusable(false);
        customEditorButton.setFocusPainted(false);
        customEditorButton.setMargin(new Insets(0, 0, 0, 0));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        final JComponent editorComp = (JComponent) editor.getTableCellEditorComponent(table, value, isSelected, row, column);
        JPanel panel = new JPanel(new BorderLayout()) {

            @Override
            public void addNotify() {
                super.addNotify();
                editorComp.requestFocus();
            }

            @Override
            protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
                InputMap map = editorComp.getInputMap(condition);
                ActionMap am = editorComp.getActionMap();

                if (map != null && am != null && isEnabled()) {
                    Object binding = map.get(ks);
                    Action action = (binding == null) ? null : am.get(binding);
                    if (action != null) {
                        return SwingUtilities.notifyAction(action, ks, e, editorComp,
                                e.getModifiers());
                    }
                }
                return false;
            }
        };
        panel.setRequestFocusEnabled(true);
        panel.add(editorComp);
        panel.add(customEditorButton, BorderLayout.EAST);
        this.table = table;
        this.row = row;
        this.column = column;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return editor.getCellEditorValue();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return editor.isCellEditable(anEvent);
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return editor.shouldSelectCell(anEvent);
    }

    @Override
    public boolean stopCellEditing() {
        return editor.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        editor.cancelCellEditing();
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        editor.addCellEditorListener(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        editor.removeCellEditorListener(l);
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        Object partialValue = editor.getCellEditorValue();
        editor.cancelCellEditing();
        editCell(table, partialValue, row, column);
    }

    protected abstract void editCell(JTable table, Object partialValue, int row, int column);
}
