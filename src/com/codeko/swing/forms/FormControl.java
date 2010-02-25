package com.codeko.swing.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 */
public class FormControl {

    Vector<PropertyChangeListener> listeners = new Vector<PropertyChangeListener>();
    JComponent base = null;
    boolean cambiado = false;
    Color colorCampoCambiado = Color.decode("#FFFFCC");
    DocumentListener docListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            gestionarCambio(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            gestionarCambio(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            gestionarCambio(e);
        }

        public void gestionarCambio(DocumentEvent e) {
            if (e.getDocument().getProperty("cdkFormTextComponent") instanceof JComponent) {
                setCambiado((JComponent) e.getDocument().getProperty("cdkFormTextComponent"));
                estadoComponenteCambiado((JComponent) e.getDocument().getProperty("cdkFormTextComponent"));
            }
        }
    };
    ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() instanceof JComponent) {
                setCambiado((JComponent) e.getSource());
                estadoComponenteCambiado((JComponent) e.getSource());
            }
        }
    };
    ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JComponent) {
                setCambiado((JComponent) e.getSource());
                estadoComponenteCambiado((JComponent) e.getSource());
            }
        }
    };
    //Como el modelo de datos de un jlist puede ser compartido por varios y no hay asociación
    //desde este hacia el JComponent tenemos que crear un mapa que los asocie
    HashMap<ListModel, Vector<JList>> modelosJList = new LinkedHashMap<ListModel, Vector<JList>>();
    ListDataListener listDataListener = new ListDataListener() {

        @Override
        public void intervalAdded(ListDataEvent e) {
            evento(e);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            evento(e);
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            evento(e);
        }

        private void evento(ListDataEvent e) {

            if (modelosJList.containsKey(e.getSource())) {
                Vector<JList> jls = modelosJList.get(e.getSource());
                for (JList j : jls) {
                    setCambiado(j);
                    estadoComponenteCambiado(j);
                }
            }
        }
    };

    public FormControl(JComponent base) {
        setBase(base);
    }

    public Color getColorCampoCambiado() {
        return colorCampoCambiado;
    }

    public void setColorCampoCambiado(Color colorCampoCambiado) {
        this.colorCampoCambiado = colorCampoCambiado;
    }

    public boolean addPropertyChangueListener(PropertyChangeListener p) {
        return listeners.add(p);
    }

    public boolean removePropertyChangueListener(PropertyChangeListener p) {
        return listeners.remove(p);
    }

    public PropertyChangeListener[] getPropertyChangueListeners() {
        return listeners.toArray(new PropertyChangeListener[]{});
    }

    public void firePropertyChangue(String property, Object oldValue, Object newValue) {
        for (PropertyChangeListener p : listeners) {
            p.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    public boolean isCambiado() {
        return cambiado;
    }

    private void setCambiado(JComponent jc) {
        //Si el componente esta cambiado es seguro que hay cambios
        if (__controlarCambios(jc)) {
            if (isValorCambiado(jc)) {
                setCambiado(true);
            } else {
                //Si no es que ha vuelto a su valor original y puede que haya cambios o no
                setCambiado(hayCambios());
            }
        }
    }

    public void setCambiado(boolean cambiado) {
        boolean old = this.cambiado;
        this.cambiado = cambiado;
        if (cambiado != old) {
            firePropertyChangue("cambiado", old, cambiado);
        }
    }

    public void resetearCambios() {
        resetearCambios(getBase());
        setCambiado(false);
    }

    public boolean hayCambios() {
        return hayCambios(getBase());
    }

    public boolean hayCambios(JComponent comp) {
        boolean ret = false;
        Component[] comps = comp.getComponents();
        for (Component c : comps) {
            if (c instanceof JComponent && !ret) {
                JComponent jc = (JComponent) c;
                if (__controlarCambios(c)) {
                    ret = isValorCambiado(jc);
                }
                if (!ret && __revisarSubcomponentes(c)) {
                    ret = hayCambios(jc);
                }
            }
            if (ret) {
                break;
            }
        }
        return ret;
    }

    private void resetearCambios(JComponent comp) {
        Component[] comps = comp.getComponents();
        for (Component c : comps) {
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                if (__controlarCambios(c)) {
                    if (jc instanceof JTextComponent) {
                        JTextComponent tc = (JTextComponent) jc;
                        jc.putClientProperty("cdkFormValor", tc.getText());
                        tc.getDocument().addDocumentListener(docListener);
                        tc.getDocument().putProperty("cdkFormTextComponent", tc);
                        resetearEstadoComponente(jc);
                    } else if (jc instanceof JToggleButton) {
                        //OJO Los toggle son itemselectable por lo que debe ir antes siempre. No se tranta como itemselectable porque afectarian los cambnios de texto
                        JToggleButton tb = (JToggleButton) jc;
                        jc.putClientProperty("cdkFormValor", tb.isSelected());
                        tb.addActionListener(actionListener);
                        resetearEstadoComponente(jc);
                    } else if (jc instanceof ItemSelectable) {
                        ItemSelectable is = (ItemSelectable) jc;
                        jc.putClientProperty("cdkFormValor", is.getSelectedObjects());
                        is.addItemListener(itemListener);
                        resetearEstadoComponente(jc);
                    } else if (jc instanceof JList) {
                        JList jl = (JList) jc;
                        jl.putClientProperty("cdkFormValor", getDatosModelo(jl.getModel()));
                        jl.getModel().addListDataListener(listDataListener);
                        if (modelosJList.containsKey(jl.getModel())) {
                            Vector<JList> v = modelosJList.get(jl.getModel());
                            if (!v.contains(jl)) {
                                v.add(jl);
                            }
                        } else {
                            Vector<JList> jls = new Vector<JList>();
                            jls.add(jl);
                            modelosJList.put(jl.getModel(), jls);
                        }
                        resetearEstadoComponente(jc);
                    }
                }
                if (__revisarSubcomponentes(c)) {
                    resetearCambios(jc);
                }
            }
        }
    }

    public boolean isValorCambiado(JComponent jc) {
        boolean iguales = true;
        Object orig = jc.getClientProperty("cdkFormValor");
        if (orig != null) {
            if (jc instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent) jc;
                iguales = tc.getText().equals(orig);
            } else if (jc instanceof JToggleButton) {
                //OJO Los toggle son itemselectable por lo que debe ir antes siempre
                JToggleButton tb = (JToggleButton) jc;
                if (orig instanceof Boolean) {
                    iguales = tb.isSelected() == ((Boolean) orig).booleanValue();
                }
            } else if (jc instanceof ItemSelectable) {
                ItemSelectable is = (ItemSelectable) jc;
                if (orig instanceof Object[]) {
                    iguales = Arrays.equals(is.getSelectedObjects(), (Object[]) orig);
                }
            } else if (jc instanceof JList) {
                JList is = (JList) jc;
                if (orig instanceof Object[]) {
                    Object[] vals = getDatosModelo(is.getModel());
                    iguales = Arrays.equals(vals, (Object[]) orig);
                }
            }
        }
        return !iguales;
    }

    private Object[] getDatosModelo(ListModel model) {
        ArrayList<Object> datos = new ArrayList<Object>(model.getSize());
        for (int i = 0; i < model.getSize(); i++) {
            datos.add(model.getElementAt(i));
        }
        return datos.toArray();
    }

    public JComponent getBase() {
        return base;
    }

    public void setBase(JComponent base) {
        this.base = base;
        resetearCambios();
    }

    public void habilitar(boolean b) {
        habilitar(b, getBase());
    }

    public void habilitar(boolean b, JComponent comp) {
        if (__aplicarEstado(comp)) {
            comp.setEnabled(b);
        }
        Component[] comps = comp.getComponents();
        for (Component c : comps) {
            if (__aplicarEstado(comp)) {
                c.setEnabled(b);
            }
            if (__revisarSubcomponentes(c)) {
                if (c instanceof JComponent) {
                    habilitar(b, (JComponent) c);
                }
            }
        }
    }

    private boolean __aplicarEstado(Component c) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (jc.getClientProperty("ckd_aplicarEstado") != null && jc.getClientProperty("ckd_aplicarEstado").equals("0")) {
                return false;
            }
        }
        return aplicarEstado(c);
    }

    private boolean __revisarSubcomponentes(Component c) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (jc.getClientProperty("ckd_revisarSubcomponentes") != null && jc.getClientProperty("ckd_revisarSubcomponentes").equals("0")) {
                return false;
            }
        }
        return revisarSubcomponentes(c);
    }

    private boolean __controlarCambios(Component c) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (jc.getClientProperty("ckd_controlarCambios") != null && jc.getClientProperty("ckd_controlarCambios").equals("0")) {
                return false;
            }
        }
        return controlarCambios(c);
    }

    public void setAplicarEstado(JComponent comp, boolean aplicar) {
        comp.putClientProperty("ckd_aplicarEstado", aplicar ? "1" : "0");
    }

    public void setRevisarSubcomponentes(JComponent comp, boolean aplicar) {
        comp.putClientProperty("ckd_revisarSubcomponentes", aplicar ? "1" : "0");
        if (!aplicar) {
            limpiarListeners(comp);
        } else {
            resetearCambios(comp);
        }
    }

    public void setControlarCambios(JComponent comp, boolean aplicar) {
        comp.putClientProperty("ckd_controlarCambios", aplicar ? "1" : "0");
        if (!aplicar) {
            limpiarListeners(comp);
        } else {
            resetearCambios(comp);
        }
    }

    private void limpiarListeners(JComponent ini) {
        if (ini instanceof JTextComponent) {
            ((JTextComponent) ini).getDocument().removeDocumentListener(docListener);
        } else if (ini instanceof JToggleButton) {
            ((JToggleButton) ini).removeActionListener(actionListener);
        } else if (ini instanceof ItemSelectable) {
            ((ItemSelectable) ini).removeItemListener(itemListener);
        } else if (ini instanceof JList) {
            ((JList) ini).getModel().removeListDataListener(listDataListener);
            JList jl = ((JList) ini);
            if (modelosJList.containsKey(jl.getModel())) {
                Vector<JList> jls = modelosJList.get(jl.getModel());
                jls.remove(jl);
                if (jls.isEmpty()) {
                    modelosJList.remove(jl.getModel());
                }
            }
        }
        Component[] comps = ini.getComponents();
        for (Component c : comps) {
            if (c instanceof JComponent) {
                limpiarListeners((JComponent) c);
            }
        }
    }

    public boolean aplicarEstado(Component c) {
        return true;
    }

    public boolean revisarSubcomponentes(Component c) {
        return true;
    }

    public boolean controlarCambios(Component c) {
        return true;
    }

    private void estadoComponenteCambiado(JComponent jc) {
        estadoComponenteCambiado(jc, isValorCambiado(jc));
    }

    public void estadoComponenteCambiado(JComponent jc, boolean cambiado) {
        Object o = jc.getClientProperty("cdkFormBackground");
        if (o == null || !(o instanceof Color)) {
            jc.putClientProperty("cdkFormBackground", jc.getBackground());
            o = jc.getBackground();
        }
        //vemos si el valor actual es igual al original
        if (cambiado) {
            //jc.setBackground(getColorCampoCambiado());
            setColorFondoComponente(jc, getColorCampoCambiado());
        } else {
            //jc.setBackground((Color) o);
            setColorFondoComponente(jc, (Color) o);
        }
    }

    public void resetearEstadoComponente(JComponent jc) {
        Object o = jc.getClientProperty("cdkFormBackground");
        if (o == null || !(o instanceof Color)) {
            jc.putClientProperty("cdkFormBackground", jc.getBackground());
        } else {
            //jc.setBackground((Color) o);
            setColorFondoComponente(jc, (Color) o);
        }
    }

    private void setColorFondoComponente(JComponent jc, Color color) {
        jc.setBackground(color);
        if (jc instanceof JComboBox) {
            JComboBox cb = (JComboBox) jc;
            if (cb.isEditable()) {
                Component editor = cb.getEditor().getEditorComponent();
                editor.setBackground(color);
                if (editor instanceof JComponent) {
                    ((JComponent) editor).setOpaque(true);
                }
            }
        }
    }
}
