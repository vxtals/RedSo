package newclient;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class LimitedPlainDocument extends PlainDocument {
    
	private static final long serialVersionUID = -1293036006252981821L;
	
	private int maxLength;
	private int lineCount = 0;

    public LimitedPlainDocument( int maxLength ) {
        this.maxLength = maxLength;
    }

    public void insertString( int offset, String str, AttributeSet a )
            throws BadLocationException {
        int length = str.length();
        if(str.compareTo("\n") == 0){
        	if(lineCount == 2){
        		return;
        	}
        	lineCount++;
        }
        if ( offset + length > maxLength )
            length = maxLength - offset;
        
        super.insertString( offset, str.substring(0, length), a );
    }
    
    public void remove(int offs,int len) throws BadLocationException{
    	if (this.getText(offs, len).compareTo("\n") == 0){
    		lineCount--;
    	}
    	super.remove(offs, len);
    }
    
    public void increaseLine(){
    	lineCount++;
    }
    
    public void resetLine(){
    	lineCount = 0;
    }
}
