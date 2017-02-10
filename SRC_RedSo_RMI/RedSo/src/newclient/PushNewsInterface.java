package newclient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota que ofrece la funcionalidad callback para el servidor de la aplicaci�n, permitiendo que est� <br>
 * notificar las novedades y las nuevas solicitudes de amistad.
 * @author V�ctor Portals Lorenzo
 * 
 */

public interface PushNewsInterface extends Remote {
	
	/**
	 * Cambia el color del bot�n de novedades (btnNovedades) declarado como un atributo de clase en la clase <br>
	 * MainClient.java. Adem�s da el valor true al flag hayNovedad (tambi�n atributo de clase en MainClient.java)<br>
	 * que evitar� que la notificaci�n desaparezca hasta que no se acceda a la secci�n de Novedades.
	 * @param email
	 * @return boolean
	 * @throws RemoteException
	 */
	
	public boolean recibirNotificacion(String email) throws RemoteException;
	/**
	  * Cambia el color del bot�n de amigos (btnAmigos) declarado como un atributo de clase en la clase <br>
	 * MainClient.java. Adem�s da el valor true al flag haySolicitud (tambi�n atributo de clase en MainClient.java)<br>
	 * que evitar� que la notificaci�n desaparezca hasta que no se acceda a la secci�n de Amigos. 
	 * @param email
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean nuevaSolicitud(String email) throws RemoteException;
	
	public String consultaIdentidad() throws RemoteException;
		
}
