package com.codeko.swing;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Gestiona la comunicación entre una tarea (Task) o cualquier WikiOffBean, una barra de progreso y un JLabel
 * @author Codeko
 */
public class CdkControlProgresos implements java.beans.PropertyChangeListener {

    JProgressBar barraActiva = null;
    JLabel labelMensajes = null;
    boolean ocultarBarraAlTerminar = false;

    public boolean isOcultarBarraAlTerminar() {
        return ocultarBarraAlTerminar;
    }

    public void setOcultarBarraAlTerminar(boolean ocultarBarraAlTerminar) {
        this.ocultarBarraAlTerminar = ocultarBarraAlTerminar;
    }

    public JProgressBar getBarraActiva() {
        return barraActiva;
    }

    public void setBarraActiva(JProgressBar barraActiva) {
        this.barraActiva = barraActiva;
    }

    public JLabel getLabelMensajes() {
        return labelMensajes;
    }

    public void setLabelMensajes(JLabel labelMensajes) {
        this.labelMensajes = labelMensajes;
    }

    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("setIniciado".equals(propertyName)) {
            if (getBarraActiva() != null) {
                getBarraActiva().setVisible(true);
                getBarraActiva().setIndeterminate(true);
            }
        } else if ("setTerminado".equals(propertyName)) {
            if (getBarraActiva() != null) {
                getBarraActiva().setIndeterminate(false);
                getBarraActiva().setValue(0);
                if (isOcultarBarraAlTerminar()) {
                    getBarraActiva().setVisible(false);
                }
            }
        } else if ("setMensaje".equals(propertyName)) {
            String text = (String) (evt.getNewValue());
            if (getBarraActiva() != null) {
                getBarraActiva().setStringPainted(true);
                getBarraActiva().setString((text == null) ? "" : text);
            }

        } else if ("setInfoExtra".equals(propertyName)) {
            String text = (String) (evt.getNewValue());
            if (getLabelMensajes() != null) {
                getLabelMensajes().setVisible(true);
                getLabelMensajes().setText((text == null) ? "" : text);
            }
        } else if ("setProgreso".equals(propertyName)) {
            if (getBarraActiva() != null) {
                if (evt.getNewValue() != null) {
                    int value = (Integer) (evt.getNewValue());
                    getBarraActiva().setVisible(true);
                    getBarraActiva().setIndeterminate(false);
                    getBarraActiva().setValue(value);
                } else {
                    getBarraActiva().setVisible(true);
                    getBarraActiva().setIndeterminate(true);
                }
            }
        } else if ("setMinimo".equals(propertyName)) {
            int value = (Integer) (evt.getNewValue());
            if (getBarraActiva() != null) {
                getBarraActiva().setMinimum(value);
            }
        } else if ("setMaximo".equals(propertyName)) {
            int value = (Integer) (evt.getNewValue());
            if (getBarraActiva() != null) {
                getBarraActiva().setMaximum(value);
            }
        } else if ("started".equals(propertyName)) {
            if (getBarraActiva() != null) {
                getBarraActiva().setVisible(true);
                getBarraActiva().setIndeterminate(true);
            }
        } else if ("done".equals(propertyName)) {
            if (getBarraActiva() != null) {
                if (isOcultarBarraAlTerminar()) {
                    getBarraActiva().setVisible(false);
                }
                getBarraActiva().setValue(0);
            }
        } else if ("message".equals(propertyName)) {
            String text = (String) (evt.getNewValue());
            if (getBarraActiva() != null) {
                getBarraActiva().setString((text == null) ? "" : text);
            }
        } else if ("progress".equals(propertyName)) {
            int value = (Integer) (evt.getNewValue());
            if (getBarraActiva() != null) {
                getBarraActiva().setVisible(true);
                getBarraActiva().setIndeterminate(false);
                getBarraActiva().setValue(value);
            }
        }
    }
}
