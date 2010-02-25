package com.codeko.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 * Esta clase gestiona de manera simple la creación de un control de look&feel.
 * Al iniciar la aplicación antes de crear ningún gráfico se debe llamar al método
 * asignarLookAndFeelGuardado(Object aplicacion)
 * Esta función asigna el look&feel guardado de previas ejecuciones.
 *
 * Además de esta funcion se proporcionan varios componentes (como un JMenu o un JComboBox) para mostrar el look&feel actual
 * y cambiarlo sobre la marcha.
 */
public class LookAndFeels {
    /**
     * Asigna el look and feel guardado para la aplicación asignada.
     * Si no hay guardado ninguno asigna el del sistem
     * @param aplicacion Objeto usado para guardar la configuración de look and feel: Preferences.userNodeForPackage(aplicacion.getClass())
     * @return true si se ha asignado correctamente, false si ha habido algún error.
     */
    public static boolean asignarLookAndFeelGuardado(Object aplicacion) {
        boolean ret = true;
        try {
            UIManager.setLookAndFeel(Preferences.userNodeForPackage(aplicacion.getClass()).get("cdk_lookandfeel", UIManager.getSystemLookAndFeelClassName()));
        } catch (Exception ex) {
            ret = false;
            Logger.getLogger(LookAndFeels.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /**
     * Devuelve un vector de acciones de cambio de look&feel
     * @param base Componente que se mandará actualizar tras cambiar el look&feel mediante SwingUtilities.updateComponentTreeUI(base);. Cualquier ventana aparte deberá actualizarse a mano
     * @param aplicacion Objeto usado para guardar la configuración de look and feel: Preferences.userNodeForPackage(aplicacion.getClass())
     * @return Vector<Action> con las acciones para los look&feels instalados
     */
    public static Vector<Action> getAccionesCambioLookAndFeel(final Component base, final Object aplicacion) {
        Vector<Action> acciones = new Vector<Action>();
        for (UIManager.LookAndFeelInfo lfi : UIManager.getInstalledLookAndFeels()) {
            Action action = new AbstractAction(lfi.getName()) {

                public void actionPerformed(ActionEvent e) {
                    try {
                        UIManager.setLookAndFeel(e.getActionCommand());
                        SwingUtilities.updateComponentTreeUI(base);
                        if (base instanceof JFrame) {
                            ((JFrame) base).pack();
                        }
                        Preferences.userNodeForPackage(aplicacion.getClass()).put("cdk_lookandfeel", e.getActionCommand());
                    } catch (Exception ex) {
                        Logger.getLogger(aplicacion.getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public String toString() {
                    return getValue(Action.NAME).toString();
                }
            };
            action.putValue(Action.ACTION_COMMAND_KEY, lfi.getClassName());
            acciones.add(action);
        }
        return acciones;
    }

    /**
     * Genera un menú con nombre Aspecto preparado para mostrar y asignar el look&feel
     * @param base Componente que se mandará actualizar tras cambiar el look&feel mediante SwingUtilities.updateComponentTreeUI(base);. Cualquier ventana aparte deberá actualizarse a mano
     * @param aplicacion Objeto usado para guardar la configuración de look and feel: Preferences.userNodeForPackage(aplicacion.getClass())
     * @return JMenu con JRadioButtonMenuItem para cada look&feel.
     */
    public static JMenu getMenuCambioLookAndFeel(final Component base, final Object aplicacion) {
        ButtonGroup bg = new ButtonGroup();
        String actual = UIManager.getLookAndFeel().getClass().getName();
        JMenu menu = new JMenu("Aspecto");

        for (Action a : getAccionesCambioLookAndFeel(base, aplicacion)) {
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem(a);
            bg.add(mi);
            if (a.getValue(Action.ACTION_COMMAND_KEY).equals(actual)) {
                mi.setSelected(true);
            }
            menu.add(mi);
        }

        return menu;
    }

    /**
     * Genera un combo box con los look&feels instalados.
     * @param base Componente que se mandará actualizar tras cambiar el look&feel mediante SwingUtilities.updateComponentTreeUI(base);. Cualquier ventana aparte deberá actualizarse a mano
     * @param aplicacion Objeto usado para guardar la configuración de look and feel: Preferences.userNodeForPackage(aplicacion.getClass())
     * @return JComboBox con los look&feels instalados.
     */
    public static JComboBox getComboCambioLookAndFeel(final Component base, final Object aplicacion) {
        String actual = UIManager.getLookAndFeel().getClass().getName();
        Vector<Action> accs = getAccionesCambioLookAndFeel(base, aplicacion);
        final JComboBox combo = new JComboBox(accs);
        int pos = 0;
        for (Action a : getAccionesCambioLookAndFeel(base, aplicacion)) {
            if (a.getValue(Action.ACTION_COMMAND_KEY).equals(actual)) {
                combo.setSelectedIndex(pos);
                break;
            }
            pos++;
        }
        combo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Action acc = (Action) combo.getSelectedItem();
                    UIManager.setLookAndFeel(acc.getValue(Action.ACTION_COMMAND_KEY).toString());
                    SwingUtilities.updateComponentTreeUI(base);
                    if (base instanceof JFrame) {
                        ((JFrame) base).pack();
                    }
                    Preferences.userNodeForPackage(aplicacion.getClass()).put("cdk_lookandfeel", acc.getValue(Action.ACTION_COMMAND_KEY).toString());
                } catch (Exception ex) {
                    Logger.getLogger(aplicacion.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return combo;
    }
}
