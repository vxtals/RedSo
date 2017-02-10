package server;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
public class IconoServidor {
	public IconoServidor() {
		//se declara el objeto tipo icono
		final TrayIcon iconoSystemTray;
		//se verifica que el SystemTray sea soportado
		if (SystemTray.isSupported()) {
			try{
				//se obtiene una instancia estática de la clase SystemTray
				SystemTray tray = SystemTray.getSystemTray();
				//esta es la imagen de icono
				BufferedImage trayIconImage;

				trayIconImage = ImageIO.read(getClass().getResource("iconoRS.png"));
				int trayIconWidth = new TrayIcon(trayIconImage).getSize().width;


				//este listener se asociara con un item del menu contextual
				//que aparece al hacer click derecho sobre el icono
				ActionListener escuchadorSalir = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				};
				//menu que aparece al hacer click derecho
				PopupMenu popup = new PopupMenu();
				iconoSystemTray = new TrayIcon(trayIconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH), "RedSo Server", popup);
				MenuItem item = new MenuItem("Cerrar servidor");
				item.addActionListener(escuchadorSalir);
				popup.add(item);
				//iniciamos el objeto TrayIcon

				
				iconoSystemTray.setImageAutoSize(true);

				//se debe capturar una excepción en caso que falle la adicion de un icono
				try {
					tray.add(iconoSystemTray);
				} catch (AWTException e) {
					System.err.println("No es posible agregar el icono al System Tray");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
			System.err.println("Tu sistema no soporta el System Tray :(");
	}

}
