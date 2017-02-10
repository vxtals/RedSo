package client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class Fuentes {
	
	public static Font title_font;
	public static Font title_font_wall;
	public static Font calibri_font;
	public static Font calibri_font_campo;
	public static Font calibri_font_tab_sel;
	public static Font calibri_font_tab_usel;
	public static Font calibri_font_name;
	public static Font button_font;
	public static Font button_font_search;
	public static Font button_font_logout;
	
	public Fuentes(){
		
		InputStream title_file = Fuentes.class.getResourceAsStream("titlefont.ttf");
		InputStream calibri_file = Fuentes.class.getResourceAsStream("calibri.ttf");
		try {
			
			title_font = Font.createFont(Font.TRUETYPE_FONT, title_file);
			title_font = title_font.deriveFont(Font.BOLD, 34f);
			title_font_wall = title_font.deriveFont(Font.BOLD, 38f);
			
			calibri_font = Font.createFont(Font.TRUETYPE_FONT, calibri_file);
			
			calibri_font = calibri_font.deriveFont(Font.PLAIN, 15f); // Los campos de texto en Login y Register
			
			calibri_font_campo = calibri_font.deriveFont(Font.BOLD, 18f);
			calibri_font_tab_sel = calibri_font.deriveFont(Font.BOLD, 28f);
			calibri_font_tab_usel = calibri_font.deriveFont(Font.PLAIN, 22f);
			calibri_font_name = calibri_font.deriveFont(Font.PLAIN, 19f);
			
			button_font = calibri_font.deriveFont(Font.BOLD, 20f);
			button_font_search = calibri_font.deriveFont(Font.BOLD, 14f);
			button_font_logout = calibri_font.deriveFont(Font.BOLD, 16f);
			
		} catch (FontFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
