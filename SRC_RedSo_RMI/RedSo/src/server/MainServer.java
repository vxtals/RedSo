package server;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;

import newclient.ErrorMsg;


public class MainServer {

	java.awt.TrayIcon trayIcon; 
	java.awt.SystemTray tray; 
	private static String hostname;
	
	public static void main (String[] args){
		
		System.out.println("RedSo Server 1.0\n Víctor Portals Lorenzo \n 2013-2014 Facultad de "
				+ "Informática - Universidad Politécnica de Madrid ");
		
		new IconoServidor();
		
		try {
			RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory());
			} catch (IOException e) {
			e.printStackTrace();
			}

		File archivo = new File ("redso_server.conf");
		FileReader lector;
		try {
			lector = new FileReader (archivo);
			BufferedReader br = new BufferedReader(lector);
			hostname = br.readLine();
			br.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			Point pos = new Point((Toolkit.getDefaultToolkit().getScreenSize().width/2), (Toolkit.getDefaultToolkit().getScreenSize().height/2));
			new ErrorMsg(pos, 6);
		}
		
		System.setProperty( "java.rmi.server.hostname" , hostname);
		
		
		try{
			
			/*if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new RMISecurityManager());
	        }*/
			
			LocateRegistry.createRegistry(1099);
			ConexionInterfaz stub = new Conexion();
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Conexion", stub);


		} catch (Exception e){
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}

	}
	

}
