package newclient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota que ofrece la funcionalidad callback para el servidor de la aplicación, permitiendo que esté <br>
 * notificar las novedades y las nuevas solicitudes de amistad.
 * @author Víctor Portals Lorenzo
 * 
 */

public interface PushNewsInterface extends Remote {
	
	/**
	 * Cambia el color del botón de novedades (btnNovedades) declarado como un atributo de clase en la clase <br>
	 * MainClient.java. Además da el valor true al flag hayNovedad (también atributo de clase en MainClient.java)<br>
	 * que evitará que la notificación desaparezca hasta que no se acceda a la sección de Novedades.
	 * @param email
	 * @return boolean
	 * @throws RemoteException
	 */
	
	public boolean recibirNotificacion(String email) throws RemoteException;
	/**
	  * Cambia el color del botón de amigos (btnAmigos) declarado como un atributo de clase en la clase <br>
	 * MainClient.java. Además da el valor true al flag haySolicitud (también atributo de clase en MainClient.java)<br>
	 * que evitará que la notificación desaparezca hasta que no se acceda a la sección de Amigos. 
	 * @param email
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean nuevaSolicitud(String email) throws RemoteException;
	
	public String consultaIdentidad() throws RemoteException;
		
}
