package com.codeko.swing;

import com.codeko.util.Num;
import java.awt.Toolkit;
import javax.swing.text.*;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 */
//TODO Mejorar esta clase
public class NumericDocument extends PlainDocument {

    protected int precision = 0;
    protected boolean permitirNegativos = false;


    public NumericDocument(int precision, boolean allowNegative) {

        super();

        this.precision = precision;

        this.permitirNegativos = allowNegative;
        
    }

    //Insert string method
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

        if (str != null) {
            if (Num.esNumero(str) == false && str.equals(",") == false && str.equals("-") == false) { //First, is it a valid character?
                Toolkit.getDefaultToolkit().beep();

                return;

            } else if (str.equals(",") == true && (super.getText(0, super.getLength()).contains(",") == true || precision==0)) { //Next, can we place a decimal here?

                Toolkit.getDefaultToolkit().beep();

                return;

            } else if (Num.esNumero(str) == true && super.getText(0, super.getLength()).indexOf(",") != -1 && offset > super.getText(0, super.getLength()).indexOf(",") && super.getLength() - super.getText(0, super.getLength()).indexOf(",") > precision && precision > 0) { //Next, do we get past the decimal precision limit?
                Toolkit.getDefaultToolkit().beep();
                return;
            } else if (str.equals("-") == true && (offset != 0 || permitirNegativos == false)) { //Next, can we put a negative sign?
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            //All is fine, so add the character to the text box
            super.insertString(offset, str, attr);
        }
        return;
    }
}
