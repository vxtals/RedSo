package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;



public class MainServer {

	java.awt.TrayIcon trayIcon; 
	java.awt.SystemTray tray; 
	private static String hostname;
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	public static ConnectionFactory connectionFactory;
	
	public static void main (String[] args){
		
		System.out.println("RedSo Server 1.0\n V�ctor Portals Lorenzo \n 2013-2014 Facultad de "
				+ "Inform�tica - Universidad Polit�cnica de Madrid ");
		
		new IconoServidor();
		
		connectionFactory = new ActiveMQConnectionFactory(url);

		File archivo = new File ("redso_server.conf");
		FileReader lector;
		try {
			lector = new FileReader (archivo);
			BufferedReader br = new BufferedReader(lector);
			hostname = br.readLine();
			br.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		System.setProperty( "java.rmi.server.hostname" , hostname );
		
		
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
