package com.codeko.swing;


public interface IObjetoTabla {
    public int getNumeroDeCampos();
    public Object getValueAt(int index);
    public String getTitleAt(int index);
    public Class getClassAt(int index);
    public boolean setValueAt(int index,Object valor);
    public boolean esCampoEditable(int index);
}
