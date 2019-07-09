import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class TextRestraint extends DocumentFilter {

    public TextRestraint() {
    	
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int currentLength = fb.getDocument().getLength();
        
        if (currentLength > 1) {
        	char newChar = text.charAt(1);
        	if ('0' <= newChar && newChar <= '9') {
        		replace(fb, offset, 1, text.substring(1), attrs);
        	} else {
        		remove(fb, offset, 1);
        	}
        } else {
        	char newChar = text.charAt(0);
        	if (!('0' <= newChar && newChar <= '9')) {
        		remove(fb, offset, 1);
        	} else {
        		replace(fb, offset, length, text, attrs);	// Registers user input
        	}
        }
    }

}
