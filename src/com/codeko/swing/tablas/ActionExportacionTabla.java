package com.codeko.swing.tablas;

import com.codeko.util.Archivo;
import com.codeko.util.Fechas;
import com.codeko.util.Str;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 */
public class ActionExportacionTabla extends AbstractAction {

    JTable tabla = null;

    public ActionExportacionTabla(JTable tabla, String name, Icon icon) {
        super(name, icon);
        this.tabla = tabla;
    }

    public ActionExportacionTabla(JTable tabla, String name) {
        super(name);
        this.tabla = tabla;
    }

    public ActionExportacionTabla(JTable tabla) {
        this.tabla = tabla;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable comp = tabla;
        try {
            JFileChooser jfc = new JFileChooser(Archivo.getUltimoArchivoUsado(e.getSource()));
            jfc.setFileFilter(new FileNameExtensionFilter("Ficheros CSV", "csv", "CSV"));

            File f = null;
            boolean cancelar = false;
            while ((f == null || f.exists()) && !cancelar) {
                int op = jfc.showSaveDialog(comp);
                if (op == JFileChooser.APPROVE_OPTION) {
                    f = jfc.getSelectedFile();
                    if (!f.getName().toLowerCase().endsWith(".csv")) {
                        f = new File(f.getParentFile(), f.getName() + ".csv");
                    }
                    if (f.exists()) {
                        int opS = JOptionPane.showConfirmDialog(comp, "El archivo '" + f.getAbsolutePath() + "' ya existe.\n¿Desea sobreescribir el archivo?", "Sobreescribir", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opS == JOptionPane.YES_OPTION) {
                            f.delete();
                        } else if (opS != JOptionPane.NO_OPTION) {
                            cancelar = true;
                        }
                    }
                } else {
                    cancelar = true;
                }
            }
            if (f != null && !f.exists() && !cancelar) {
                Archivo.setUltimoArchivoUsado(e.getSource(), f);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
                for (int r = -1; r < comp.getRowCount(); r++) {
                    StringBuilder sb = new StringBuilder();
                    for (int c = 0; c < comp.getColumnCount(); c++) {
                        if (r >= 0) {
                            if (c != 0) {
                                sb.append(";");
                            }
                            sb.append("\"");
                            Object valor = comp.getValueAt(r, c);
                            if (valor instanceof Boolean) {
                                valor = (((Boolean) valor) ? "Si" : "No");
                            } else if (valor instanceof Calendar || valor instanceof Date) {
                                valor = Fechas.format(valor);
                            }
                            sb.append(Str.noNulo(valor).replace("\"", "\\\""));
                            sb.append("\"");
                        } else {
                            if (c != 0) {
                                sb.append(";");
                            }
                            sb.append("\"");
                            sb.append(Str.noNulo(comp.getTableHeader().getColumnModel().getColumn(c).getHeaderValue()).replace("\"", "\\\""));
                            sb.append("\"");
                        }
                    }
                    sb.append("\r\n");
                    bw.write(sb.toString());
                }
                bw.close();
                int opA = JOptionPane.showOptionDialog(comp, "Tabla exportada correctamente en:\n" + f.getAbsolutePath(), "Exportación realizada", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Abrir archivo", "Aceptar"}, "Aceptar");
                if (opA == JOptionPane.OK_OPTION) {
                    Desktop.getDesktop().open(f);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ActionExportacionTabla.class.getName()).log(Level.WARNING, "Error exportando tabla", ex);
            JOptionPane.showMessageDialog(comp, "Se ha producido un error escribiendo en el fichero:\n" + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
