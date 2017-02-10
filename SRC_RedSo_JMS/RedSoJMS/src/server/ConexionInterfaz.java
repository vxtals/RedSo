package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota que ofrece una funcionalidad b�sica previa a la creaci�n de la sesi�n. Estas funcionalidades <br>
 * son las de inicio de sesi�n y registro.
 * @author V�ctor Portals Lorenzo
 *
 */

	public interface ConexionInterfaz extends Remote {
	
	/**
	 * Compueba que el usuario y contrase�a sean correctos y crea el objeto remoto Sesion asociado con el email que recibe como par�metro.
	 * Adem�s se encarga de almacenar la direcci�n IPv4 del usuario para las funciones de callback.
	 * @param email
	 * @param contrase�a
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean iniciarSesion(String email, String pass) throws RemoteException;
	/**
	 * Crea un nuevo objeto User con los par�metros recibidos y llama al m�todo inciarSesion.
	 * @param email
	 * @param name
	 * @param apellidos
	 * @param contrase�a
	 * @param foto
	 * @param web
	 * @param publico
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean crearUsuario(String email, String name, String apellidos, String pass, byte[] foto, String web, boolean publico) throws RemoteException;
	
	
}
