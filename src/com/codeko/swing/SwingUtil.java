package com.codeko.swing;

import com.codeko.swing.tablas.MouseListenerOpcionesTabla;
import com.codeko.util.Fechas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 */
public class SwingUtil {

    public static void expandirJTree(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandirJTree(tree, new TreePath(root), expand);
    }

    private static void expandirJTree(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandirJTree(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    public static void addMenuCopiarPegar(JTextComponent componente) {
        CB.addMenuCopiarPegar(componente);
    }

    public static JPopupMenu addMenuImpresion(JTable tabla, String nombreImpresion, String cabecera, String pie) {
        MouseListenerOpcionesTabla a = new MouseListenerOpcionesTabla(tabla, false, nombreImpresion, cabecera, pie);
        tabla.addMouseListener(a);
        return a.getMenu();
    }

    public static JPopupMenu addMenuTabla(JTable tabla, boolean mostraOpcionExportar, String nombreImpresion, String cabeceraImpresion) {
        MouseListenerOpcionesTabla a = new MouseListenerOpcionesTabla(tabla, mostraOpcionExportar, nombreImpresion, cabeceraImpresion, "Página {0} - " + Fechas.format(new GregorianCalendar()));
        tabla.addMouseListener(a);
        return a.getMenu();
    }

    public static void blink(final JComponent componente) {
        blink(componente, Color.RED, 3, 300);
    }

    public static void blink(final JComponent componente, final Color color, int blinks, int delay) {
        final Color original = componente.getBackground();
        final boolean opaco = componente.isOpaque();
        final int veces = blinks + 2;
        final Timer t = new Timer(delay, new ActionListener() {

            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                componente.setOpaque(true);
                if (count > veces) {
                    componente.setOpaque(opaco);
                    componente.setBackground(original);
                    System.out.println(e.getSource().getClass().getName());
                    if (e.getSource() instanceof Timer) {
                        ((Timer) e.getSource()).stop();
                    }
                } else {
                    if (componente.getBackground() == original) {
                        componente.setBackground(color);
                    } else {
                        componente.setBackground(original);
                    }
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }
}

