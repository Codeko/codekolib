package com.codeko.swing;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Vector;

/**
 *
 * @author Codeko
 */
public class CodekoTableModel<T extends IObjetoTabla> extends javax.swing.table.DefaultTableModel {

    IObjetoTabla objetoModelo = null;
    Vector<T> datos = null;
    boolean editable = true;

    public CodekoTableModel(T modelo) {
        super();
        setObjetoModelo(modelo);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Usado SOLO para enlazar el vector de datos con un vecto externo
     * @param datos
     */
    public void setDatos(Vector<T> datos) {
        this.datos = datos;
        fireTableDataChanged();
    }

    public Vector<T> getDatos() {
        if (datos == null) {
            datos = new Vector<T>();
        }
        return datos;
    }

    private IObjetoTabla getObjetoModelo() {
        return objetoModelo;
    }

    public void setObjetoModelo(T objetoModelo) {
        this.objetoModelo = objetoModelo;
        this.datos = null;
        fireTableStructureChanged();
    }

    public void addDato(T c) {
        if (!getDatos().contains(c)) {
            getDatos().addElement(c);
            fireTableDataChanged();
        }
    }

    public void quitarDato(T e) {
        if (getDatos().remove(e)) {
            fireTableDataChanged();
        }
    }

    public void quitarDatos(Collection<T> datos) {
        if (getDatos().removeAll(datos)) {
            fireTableDataChanged();
        }
    }

    public T getElemento(int posicion) {
        return getDatos().elementAt(posicion);
    }

    public int indexOf(T dato) {
        return getDatos().indexOf(dato);
    }

    public void addDatos(Collection<T> datos) {
        boolean hayCambios = false;
        LinkedHashSet<T> lhs = new LinkedHashSet<T>(getDatos());
        hayCambios = lhs.addAll(datos);
        if (hayCambios) {
            getDatos().clear();
            getDatos().addAll(lhs);
            fireTableDataChanged();
        }
    }

    public void vaciar() {
        getDatos().clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return getDatos().size();
    }

    @Override
    public int getColumnCount() {
        return getObjetoModelo().getNumeroDeCampos();
    }

    @Override
    public Class getColumnClass(int index) {
        return getObjetoModelo().getClassAt(index);
    }

    @Override
    public String getColumnName(int index) {
        return getObjetoModelo().getTitleAt(index);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object val = null;
        if (getRowCount() > row) {
            IObjetoTabla ot = getDatos().elementAt(row);
            val = ot.getValueAt(col);
        }
        return val;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (isEditable()) {
            return getElemento(row).esCampoEditable(column);
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (getRowCount() > row) {
            IObjetoTabla ot = getDatos().elementAt(row);
            if (ot.setValueAt(column, aValue)) {
                fireTableDataChanged();
            }
        }
    }
}
