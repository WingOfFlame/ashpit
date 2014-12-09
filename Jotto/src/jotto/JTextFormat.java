/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Justin Hu
 */
public class JTextFormat extends PlainDocument {

    private int limit;

    JTextFormat(int limit) {
        super();
        this.limit = limit;
    }

    JTextFormat(int limit, boolean upper) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            str = str.toUpperCase();
            super.insertString(offset, str, attr);
        }
    }

}
