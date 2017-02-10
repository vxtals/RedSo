package newclient;


import java.io.Serializable;
import java.rmi.RemoteException;

public class PushNews implements PushNewsInterface, Serializable {

	private static final long serialVersionUID = -3117307559981749377L;
	
	private MainWindow mainFrame;
	
	public PushNews(MainWindow mainFrame) throws RemoteException {
		this.mainFrame = mainFrame;
	}
	
	public boolean recibirNotificacion(String email) throws RemoteException {
		mainFrame.newsNotification();
		return true;
	}

	public boolean nuevaSolicitud(String email) throws RemoteException {
		System.out.println("Se ha recibido una nueva solicitud");
		mainFrame.requestNotification();
		return true;
	}

	public String consultaIdentidad() throws RemoteException {
		return mainFrame.getEmail();
	}

}
