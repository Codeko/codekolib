package com.codeko.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 * Clase encargarda de gestionar las operaciones más comunes relacionadas con la interfaz gráfica.
 * Actualmente esta clase gestiona principalmente posicionamiento de ventanas y diálogos en la pantalla y entre sí.
 * @author Boris Burgos García
 * @version 1.0
 */
public class GUI {

    private static Logger log = Logger.getLogger(GUI.class.toString());
    private static Level level = Level.FINEST;

    /**
     * Asigna un tamaño de fuente a todos los componentes de la interfaz gráfica
     * @param size float con el tamaño a tomar
     * Esta funcion cambia la fuente de todos los camponentes a la que tengan los JLabel pero con el tamaño cambiado a size
     */
    public static void setTamanoDeFuenteGlobal(float size) {
        //System.setProperty("swing.useSystemFontSettings","false");
        Font font = (((Font) UIManager.get("Label.font")).deriveFont(size));
        for (Enumeration e = UIManager.getDefaults().keys(); e.hasMoreElements();) {
            Object key = e.nextElement();
            Object value = UIManager.get(key);
            log.log(level, "Key: '" + key + "' Value:'" + value + "'");
            //Comprobamos el nombre porque algunas cclaves tienen como valor null por lo que el instenceof font no vale
            if (value instanceof Font || key.toString().toLowerCase().indexOf("font") != -1) {
                UIManager.put(key, font);
                log.log(level, "Cambiado valor de Key: '" + key + "' de Value:'" + UIManager.get(key) + "' a '" + font + "'");
            }
        }
        //Asignamos a mano algunos porque no todas las claves estan en el default no se porqué
        String[] keys = {
            "Button.font",
            "CheckBox.font",
            "ColorChooser.font",
            "ComboBox.font",
            "EditorPane.font",
            "FormattedTextField.font",
            "Label.font",
            "List.font",
            "Panel.font",
            "PasswordField.font",
            "ProgressBar.font",
            "RadioButton.font",
            "ScrollPane.font",
            "Spinner.font",
            "TabbedPane.font",
            "Table.font",
            "TableHeader.font",
            "TextField.font",
            "TextPane.font",
            "ToolBar.font",
            "ToggleButton.font",
            "Tree.font",
            "Viewport.font",
            "InternalFrame.titleFont",
            "OptionPane.font",
            "OptionPane.messageFont",
            "OptionPane.buttonFont",
            "Spinner.font",
            "TextArea.font",
            "TitledBorder.font",
            "ToolTip.font",
            "CheckBoxMenuItem.font",
            "CheckBoxMenuItem.acceleratorFont",
            "Menu.font",
            "Menu.acceleratorFont",
            "MenuBar.font",
            "MenuItem.font",
            "MenuItem.acceleratorFont",
            "PopupMenu.font",
            "RadioButtonMenuItem.font",
            "RadioButtonMenuItem.acceleratorFont"
        };
        for (String k : keys) {
            UIManager.put(k, font);
            log.log(level, "Cambio forzado de valor de Key: '" + k + "' de Value:'" + UIManager.get(k) + "' a '" + font + "'");
        }
    }

    /**
     * Coloca a un componente sobre su parent coincidiendo sus esquinas superiores izquierdas + el margen especificado
     * @param componente Componente a posicionar
     * @param margenX Margen en el eje X
     * @param margenY Margen en el eje Y
     */
    public static void superponer(Component componente, int margenX, int margenY) {
        superponer(null, componente, margenX, margenY);
    }

    /**
     * Coloca a un componente sobre otro especificado coincidiendo sus esquinas superiores izquierdas + el margen especificado
     * @param padre Componente sobre el que se colocará el otro
     * @param componente Componente a posicionar
     * @param margenX Margen en el eje X
     * @param margenY Margen en el eje YY
     */
    public static void superponer(Component padre, Component componente, int margenX, int margenY) {
        if (componente != null) {
            if (padre == null) {
                padre = componente.getParent();
            }
            if (padre != null) {
                //Según el nuevo componente sea una ventana o no hay que usar unas medidas u otras
                Point locPadre = padre.getLocation();
                if (componente instanceof Window) {
                    locPadre = padre.getLocationOnScreen();
                }
                componente.setLocation((int) locPadre.getX() + margenX, (int) locPadre.getY() + margenY);
            }
        }
    }

    /**
     * Coloca a un componente sobre otro en posicion de cascada (coinciciendo la esquina inferior derecha del trasero con la superior izquierda
     * del delantero superponiedose el margen especificado)
     * @param trasero Componente trasero
     * @param componente Componente delantero
     * @param margenX Superposición en el eje X
     * @param margenY Superposición en el eje Y
     */
    public static void cascada(Component trasero, Component componente, int margenX, int margenY) {
        if (componente != null) {
            if (trasero != null) {
                //Según el nuevo componente sea una ventana o no hay que usar unas medidas u otras
                Point p = trasero.getLocation();
                if (componente instanceof Window) {
                    p = trasero.getLocationOnScreen();
                }
                int x = (int) p.getX() + trasero.getWidth() - margenX;
                int y = (int) p.getY() + trasero.getHeight() - margenY;
                componente.setLocation(x, y);
            }
        }
    }

    /**
     * Centra un componente en la pantalla
     * @param componente Componente a centrar
     */
    public static void centrar(Component componente) {
        centrar(null, componente);
    }

    /**
     * Centra un componente respecto a otro
     * @param trasero Componente sobre el que se centrara el componente. Si es nulo se usará la pantalla como referencia de centrado
     * @param componente Componente a centrar sobre el trasero
     */
    public static void centrar(Component trasero, Component componente) {
        if (componente == null) {
            return;
        }
        Rectangle res = null;
        if (trasero != null) {
            res = trasero.getBounds();
        } else {
            res = new Rectangle(0, 0, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        }
        int posX = (int) ((int) ((res.getCenterX()) - componente.getWidth() / 2) + res.getX());
        int posY = (int) (((res.getHeight() / 2) - componente.getHeight() / 2) + res.getY());
        componente.setLocation(posX, posY);
    }
}
