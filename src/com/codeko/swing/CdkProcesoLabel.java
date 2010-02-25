package com.codeko.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class CdkProcesoLabel extends JLabel {

    private final Timer busyIconTimer;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private int rationAnimacion = 30;

    public int getRationAnimacion() {
        return rationAnimacion;
    }

    public void setRationAnimacion(int rationAnimacion) {
        this.rationAnimacion = rationAnimacion;
    }

    public void setOK(boolean ok) {
        if (ok) {
            setIcon(new ImageIcon(CdkProcesoLabel.class.getResource("accept.png")));
        }else{
            setIcon(new ImageIcon(CdkProcesoLabel.class.getResource("cross.png")));
        }
    }

    public CdkProcesoLabel() {
        super();
        int busyAnimationRate = getRationAnimacion();
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = new ImageIcon(CdkProcesoLabel.class.getResource("busyicons/busy-icon" + i + ".png"));
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                setIcon(busyIcons[busyIconIndex]);
            }
        });
    }

    public void setProcesando(boolean procesando) {
        if (procesando) {
            if (!busyIconTimer.isRunning()) {
                setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }
        } else {
            busyIconTimer.stop();
            setIcon(null);
        }
    }

    public void setProcesando(boolean procesando, boolean okFinal) {
        if (procesando) {
            if (!busyIconTimer.isRunning()) {
                setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }
        } else {
            busyIconTimer.stop();
            setOK(okFinal);
        }
    }
}
