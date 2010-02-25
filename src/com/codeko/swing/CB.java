package com.codeko.swing;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class CB implements ClipboardOwner {

    private static CB clip = null;

    private CB() {
    }

    public static CB getClip() {
        if (clip == null) {
            clip = new CB();
        }
        return clip;
    }

    public static void setTexto(String texto) {
        getClip().setClipboardContents(texto);
    }

    public static String getTexto() {
        return getClip().getClipboardContents();
    }

    public static void addMenuCopiarPegar(final JTextComponent componente) {
        componente.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    JPopupMenu m = new JPopupMenu();
                    JMenuItem mCopiar = new JMenuItem("Copiar");
                    mCopiar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            CB.setTexto(componente.getSelectedText());
                        }
                    });
                    m.add(mCopiar);
                    mCopiar.setEnabled(componente.getSelectedText() != null);
                    mCopiar.setIcon(new ImageIcon(CB.class.getResource("page_copy.png")));
                    JMenuItem mCortar = new JMenuItem("Cortar");
                    mCortar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            CB.setTexto(componente.getSelectedText());
                            int pos = componente.getSelectionStart();
                            StringBuffer sb = new StringBuffer(componente.getText());
                            sb.delete(componente.getSelectionStart(), componente.getSelectionEnd());

                            componente.setText(sb.toString());
                            if (pos >= componente.getText().length()) {
                                pos = componente.getText().length() - 1;
                            }
                            componente.setCaretPosition(pos);
                        }
                    });
                    m.add(mCortar);
                    mCortar.setEnabled(componente.getSelectedText() != null);
                    mCortar.setIcon(new ImageIcon(CB.class.getResource("cut.png")));
                    JMenuItem mPegar = new JMenuItem("Pegar");
                    mPegar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            StringBuffer sb = new StringBuffer(componente.getText());
                            sb.replace(componente.getSelectionStart(), componente.getSelectionEnd(), CB.getTexto());
                            int pos = componente.getSelectionStart();
                            componente.setText(sb.toString());
                            if (pos >= componente.getText().length()) {
                                pos = componente.getText().length() - 1;
                            }
                            componente.setCaretPosition(pos);
                        }
                    });
                    m.add(mPegar);
                    mPegar.setEnabled(componente.isEditable());
                    mPegar.setIcon(new ImageIcon(CB.class.getResource("page_paste.png")));
                    m.show((Component) evt.getSource(), evt.getX(), evt.getY());
                }
            }
        });
    }

    public void setClipboardContents(String aString) {
        StringSelection stringSelection = new StringSelection(aString);
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    public String getClipboardContents() {
        String result = "";
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(CB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
} 

