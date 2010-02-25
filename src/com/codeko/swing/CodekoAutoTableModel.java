package com.codeko.swing;

import com.codeko.util.Num;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Codeko
 */
public class CodekoAutoTableModel<T> extends javax.swing.table.DefaultTableModel {

    Class claseModelo = null;
    Vector<T> datos = null;
    Vector<ObjCdkAutoCol> defs = null;

    public CodekoAutoTableModel(Class claseModelo) {
        super();
        setClaseModelo(claseModelo);
    }

    /**
     * Usado SOLO para enlazar el vector de datos con un vecto externo
     * @param datos
     */
    @SuppressWarnings("unchecked")
    public void setDatos(Vector<T> datos) {
        this.datos = (Vector<T>) datos.clone();
        fireTableDataChanged();
    }

    public Vector<T> getDatos() {
        if (datos == null) {
            datos = new Vector<T>();
        }
        return datos;
    }

    public Class getClaseModelo() {
        return claseModelo;
    }

    public void setClaseModelo(Class claseModelo) {
        this.claseModelo = claseModelo;
        this.datos = null;
        this.defs = null;
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

    private Vector<ObjCdkAutoCol> getDefs() {
        verificarEstructura();
        return defs;
    }

    private void procesarClase(Class clase) throws SecurityException {
        Boolean editablePorDefecto = null;
        boolean addSinAnotacion = true;
        @SuppressWarnings(value = "unchecked")
        CdkAutoTabla ta = (CdkAutoTabla) clase.getAnnotation(CdkAutoTabla.class);
        short editable = CdkAutoTabla.EDITABLE_AUTO;
        boolean autoTitulos = true;
        if (ta != null) {
            if (ta.procesarPadre()) {
                procesarClase(clase.getSuperclass());
            }
            editable = ta.editable();
            switch (ta.editable()) {
                case CdkAutoTabla.EDITABLE_NO:
                    editablePorDefecto = false;
                    break;
                case CdkAutoTabla.EDITABLE_SI:
                    editablePorDefecto = true;
                    break;
            }
            addSinAnotacion = ta.mostrarTodos();
            autoTitulos = ta.autoTitulo();
        }
        Field[] attr = clase.getDeclaredFields();
        for (int i = 0; i < attr.length; i++) {
            if (esAccesible(clase, attr[i].getName())) {
                ObjCdkAutoCol def = new ObjCdkAutoCol();
                CdkAutoTablaCol cat = attr[i].getAnnotation(CdkAutoTablaCol.class);
                if (cat != null) {
                    if (!cat.ignorar()) {
                        def.setPeso(cat.peso());
                        if (cat.titulo() != null) {
                            def.setTitulo(cat.titulo());
                        } else {
                            if (autoTitulos) {
                                def.setTitulo(getAutoTitulo(attr[i].getName()));
                            } else {
                                def.setTitulo(attr[i].getName());
                            }
                        }
                        def.setClase(attr[i].getType());
                        Boolean editableCampo = null;
                        switch (cat.editable()) {
                            case CdkAutoTabla.EDITABLE_NO:
                                editableCampo = false;
                                break;
                            case CdkAutoTabla.EDITABLE_SI:
                                editableCampo = true;
                                break;
                            default:
                                //por defecto le asignamos el del
                                switch (editable) {
                                    case CdkAutoTabla.EDITABLE_NO:
                                        editableCampo = false;
                                        break;
                                    case CdkAutoTabla.EDITABLE_SI:
                                        editableCampo = true;
                                        break;
                                    case CdkAutoTabla.EDITABLE_AUTO:
                                        editableCampo = isEditable(clase, attr[i].getName());
                                        break;
                                }
                        }
                        def.setEditable(editableCampo);
                        def.setCampo(attr[i].getName());
                        getDefs().add(def);
                    }
                } else if(addSinAnotacion) {
                    if (autoTitulos) {
                        def.setTitulo(getAutoTitulo(attr[i].getName()));
                    } else {
                        def.setTitulo(attr[i].getName());
                    }
                    def.setClase(attr[i].getType());
                    if (editablePorDefecto == null) {
                        editablePorDefecto = isEditable(clase, attr[i].getName());
                    }
                    def.setEditable(editablePorDefecto);
                    def.setCampo(attr[i].getName());
                    getDefs().add(def);
                }


            }

        }
        Method[] funciones = clase.getDeclaredMethods();
        for (Method f : funciones) {
            CdkAutoTablaCol cat = f.getAnnotation(CdkAutoTablaCol.class);

            if (cat != null && Modifier.isPublic(f.getModifiers()) && !cat.ignorar()) {
                ObjCdkAutoCol def = new ObjCdkAutoCol();
                if (cat.titulo() != null) {
                    def.setTitulo(cat.titulo());
                } else {
                    if (autoTitulos) {
                        def.setTitulo(getAutoTitulo(f.getName()));
                    } else {
                        def.setTitulo(f.getName());
                    }
                }
                def.setClase(f.getReturnType());
                //TODO Implementar el que si sean editables para funciones tambien
                def.setEditable(false);
                def.setCampo(f.getName());
                def.setPeso(cat.peso());
                getDefs().add(def);
            }
        }
        Collections.sort(getDefs());
    }

    private void verificarEstructura() {
        if (this.defs == null) {
            this.defs = new Vector<ObjCdkAutoCol>();
            procesarClase(getClaseModelo());
        }
    }

    private String getAutoTitulo(String nombre) {
        StringBuilder sb = new StringBuilder();
        Boolean modoNumeros = null;
        for (int i = 0; i < nombre.length(); i++) {
            char c = nombre.charAt(i);

            //Si es el primer caracter va en mayusculas
            if (i == 0) {
                c = Character.toUpperCase(c);
            }
            //Si pasamos de número a letra o viceversa añadimos un espacio
            if (Num.esNumero(c)) {
                if (modoNumeros != null && !modoNumeros) {
                    sb.append(" ");
                }
                modoNumeros = true;
            } else {
                if (modoNumeros != null && modoNumeros) {
                    sb.append(" ");
                    c = Character.toUpperCase(c);
                } else {
                    if (i > 0) {
                        char ant = nombre.charAt(i - 1);
                        //Si el caracter es mayusculas y el anterio no lo era
                        if (c == Character.toUpperCase(c) && ant != Character.toUpperCase(ant)) {
                            sb.append(" ");
                        }
                    }
                }
                modoNumeros = false;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public int getColumnCount() {
        return getDefs().size();
    }

    @Override
    public Class getColumnClass(int index) {
        return getDefs().elementAt(index).getClase();
    }

    @Override
    public String getColumnName(int index) {
        return getDefs().elementAt(index).getTitulo();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object val = null;
        if (getRowCount() > row) {
            try {
                val = getValor(getDatos().elementAt(row), getDefs().elementAt(col).getCampo());
            } catch (Exception ex) {
                Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, "Error buscando campo " + getDefs().elementAt(col).getCampo() + " en " + getDatos().elementAt(row) + " clase: " + getDatos().elementAt(row).getClass().getName(), ex);
            }
        }
        return val;
    }

    private Object getValor(Object obj, String campo) {
        //Primero miramos si es simplemente una funcion
        try {
            return obj.getClass().getMethod(campo, new Class[]{}).invoke(obj, new Object[]{});
        } catch (Exception ex) {
            // Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Primero vemos si hay un método getCampo
        String get = "get" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        try {
            return obj.getClass().getMethod(get, new Class[]{}).invoke(obj, new Object[]{});
        } catch (Exception ex) {
            // Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        String is = "is" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        try {
            return obj.getClass().getMethod(is, new Class[]{}).invoke(obj, new Object[]{});
        } catch (Exception ex) {
            // Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return obj.getClass().getField(campo).get(obj);
        } catch (Exception ex) {
            // Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private boolean isEditable(Class clase, String campo) {
        String set = "set" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        //Ahora necesitamos la clase del field
        Field fCampo = null;
        Field attr[] = clase.getDeclaredFields();
        for (Field f : attr) {
            if (f.getName().equals(campo)) {
                fCampo = f;
                break;
            }
        }
        if (fCampo != null) {
            try {
                clase.getMethod(set, fCampo.getType());
                return true;
            } catch (Exception ex) {
                //Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    private boolean setValor(Object elemento, String campo, Object valor) {
        String set = "set" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        //Ahora necesitamos la clase del field
        Field fCampo = null;
        Field attr[] = elemento.getClass().getDeclaredFields();
        for (Field f : attr) {
            if (f.getName().equals(campo)) {
                fCampo = f;
                break;
            }
        }
        if (fCampo != null) {
            try {
                Object retorno = elemento.getClass().getMethod(set, fCampo.getType()).invoke(elemento, valor);
                if (retorno instanceof Boolean) {
                    return (Boolean) retorno;
                }
                return true;
            } catch (Exception ex) {
                //Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                elemento.getClass().getField(campo).set(elemento, valor);
                return true;
            } catch (Exception ex) {
                //Logger.getLogger(CodekoAutoTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @SuppressWarnings({"unchecked", "unchecked"})
    private boolean esAccesible(Class clase, String campo) {
        String get = "get" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        try {
            clase.getMethod(get, new Class[]{});
            return true;
        } catch (Exception ex) {
        }
        get = "is" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        try {
            clase.getMethod(get, new Class[]{});
            return true;
        } catch (Exception ex) {
        }
        try {
            clase.getField(campo);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isCellEditable(int row, int column) {
        if (getDefs().get(column).getEditable() == null) {
            String campo = getDefs().elementAt(column).getCampo();
            String set = "set" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
            try {
                getClaseModelo().getMethod(set, new Class[]{getDefs().elementAt(column).getClase()});
                getDefs().elementAt(column).setEditable(true);
            } catch (Exception ex) {
                getDefs().elementAt(column).setEditable(false);
            }
        }
        return getDefs().get(column).getEditable();
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {
        if (getRowCount() > row) {
            if (setValor(getElemento(row), getDefs().elementAt(col).getCampo(), aValue)) {
                elementoModificado(getElemento(row), col, aValue);
                fireTableCellUpdated(row, col);
            }
        }
    }

    public void elementoModificado(T elemento, int col, Object valor) {
    }
}

class ObjCdkAutoCol implements Comparable<ObjCdkAutoCol> {

    Class clase = null;
    String campo = null;
    String titulo = null;
    Boolean editable = null;
    int peso = 0;

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Class getClase() {
        return clase;
    }

    public void setClase(Class clase) {
        this.clase = clase;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public int compareTo(ObjCdkAutoCol o) {
        return new Integer(getPeso()).compareTo(new Integer(o.getPeso()));
    }
}
