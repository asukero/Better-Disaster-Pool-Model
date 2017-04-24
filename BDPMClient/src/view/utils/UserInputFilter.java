package view.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Documentfilter pour n'entrer que des caractères alpha numériques dans une Jtextfield
 */
public class UserInputFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, string.replaceAll("([^0-9A-Za-z@.]{1,255})$", ""), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length,
                        String string, AttributeSet attr) throws BadLocationException {
        super.replace(fb, offset, length, string.replaceAll("([^0-9A-Za-z@.]{1,255})$", ""), attr);
    }

}