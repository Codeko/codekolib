/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeko.swing.tablas;

import com.codeko.swing.SwingUtil;
import com.codeko.util.Str;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingUtilities;

/**
 *
 * @author codeko
 */
public class MouseListenerOpcionesTabla extends MouseAdapter {

    JTable tabla = null;
    String nombreImpresion = "";
    String cabecera = "";
    String pie = "";
    boolean mostrarExportar = false;

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getNombreImpresion() {
        return nombreImpresion;
    }

    public void setNombreImpresion(String nombreImpresion) {
        this.nombreImpresion = nombreImpresion;
    }

    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }

    public JTable getTabla() {
        return tabla;
    }

    public void setTabla(JTable tabla) {
        this.tabla = tabla;
    }

    

    public MouseListenerOpcionesTabla(JTable tabla, boolean mostraOpcionExportar, String nombreImpresion, String cabecera, String pie) {
        this.tabla = tabla;
        this.nombreImpresion = nombreImpresion;
        this.cabecera = cabecera;
        this.pie = pie;
        this.mostrarExportar = mostraOpcionExportar;
        crearMenu();
    }

    public boolean isMostrarExportar() {
        return mostrarExportar;
    }

    JPopupMenu menu = new JPopupMenu();

    public JPopupMenu getMenu() {
        return menu;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            menu.show((Component) e.getSource(), e.getX(), e.getY());
        }
    }

    private void crearMenu() {
        JMenuItem imprimir = new JMenuItem("Imprimir", 'i');
        imprimir.setIcon(new ImageIcon(SwingUtil.class.getResource("printer.png")));
        imprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionImpresion(e);
            }
        });
        menu.add(imprimir);
        if (isMostrarExportar()) {
            JMenuItem exp = new JMenuItem(new ActionExportacionTabla(tabla, "Exportar", new ImageIcon(SwingUtil.class.getResource("page_excel.png"))));
            menu.add(exp);
        }

    }

    public void accionImpresion(ActionEvent e) {
        try {
            //http://java.sun.com/j2se/1.4.2/docs/api/javax/print/attribute/PrintRequestAttribute.html
            HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new JobName(Str.noNulo(nombreImpresion), Locale.getDefault()));
            tabla.print(PrintMode.FIT_WIDTH, cabecera != null ? new MessageFormat(cabecera) : null, pie != null ? new MessageFormat(pie) : null, true, pras, true);
        } catch (Exception ex) {
            Logger.getLogger(SwingUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
