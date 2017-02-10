package client;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.SwingUtilities;

public class MainClient {
	
	public static String host;// = "127.0.0.1"; // Dirección host por defecto.

	public static void main (String[] args){

		new Fuentes();

		File archivo = new File ("redso_client.conf");
		FileReader lector;
		try {
			lector = new FileReader (archivo);
			BufferedReader br = new BufferedReader(lector);
			host = br.readLine();
			br.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			Point pos = new Point((Toolkit.getDefaultToolkit().getScreenSize().width/2), (Toolkit.getDefaultToolkit().getScreenSize().height/2));
			new ErrorMsg(pos, 6);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point pos = new Point((Toolkit.getDefaultToolkit().getScreenSize().width/2), (Toolkit.getDefaultToolkit().getScreenSize().height/2));
				new Login(pos);
			}
		});
	}
}
