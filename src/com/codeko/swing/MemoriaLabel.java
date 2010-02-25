package com.codeko.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;
import javax.swing.Timer;
import javax.swing.JLabel;

public class MemoriaLabel extends JLabel {

    private boolean mostrarLibre = true;
    private boolean mostrarMaxima = true;
    private boolean mostrarTotal = true;

    public MemoriaLabel() {
        super();
        if (!Beans.isDesignTime()) {
            t.setRepeats(true);
            t.start();
        }
    }

    public boolean isMostrarLibre() {
        return mostrarLibre;
    }

    public void setMostrarLibre(boolean mostrarLibre) {
        this.mostrarLibre = mostrarLibre;
    }

    public boolean isMostrarMaxima() {
        return mostrarMaxima;
    }

    public void setMostrarMaxima(boolean mostrarMaxima) {
        this.mostrarMaxima = mostrarMaxima;
    }

    public boolean isMostrarTotal() {
        return mostrarTotal;
    }

    public void setMostrarTotal(boolean mostrarTotal) {
        this.mostrarTotal = mostrarTotal;
    }
    private Timer t = new Timer(1000, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Runtime r = Runtime.getRuntime();
            long free = r.freeMemory();
            long max = r.maxMemory();
            long total = r.totalMemory();
            StringBuilder sb = new StringBuilder();
            if (isMostrarLibre()) {
                sb.append(formatearMemoria(free));
                sb.append("/");
            }
            if (isMostrarTotal()) {
                sb.append(formatearMemoria(total));
            }
            if (isMostrarMaxima()) {
                boolean hayDatos = sb.length() > 0;
                if (hayDatos) {
                    sb.append(" [");
                }
                sb.append(formatearMemoria(max));
                if (hayDatos) {
                    sb.append("]");
                }
            }
            setText(sb.toString().trim());
        }
    });

    private String formatearMemoria(long bytes) {
        String res = null;
        if (bytes > 1024) {
            if (bytes > (1024 * 1024)) {
                if (bytes > (1024 * 1024 * 1024)) {
                    bytes = bytes / (1024 * 1024 * 1024);
                    res = bytes + "GB";
                } else {
                    bytes = bytes / (1024 * 1024);
                    res = bytes + "MB";
                }
            } else {
                bytes = bytes / 1024;
                res = bytes + "KB";
            }
        } else {
            res = bytes + "B";
        }

        return res;
    }
}
