package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota que ofrece una funcionalidad básica previa a la creación de la sesión. Estas funcionalidades <br>
 * son las de inicio de sesión y registro.
 * @author Víctor Portals Lorenzo
 *
 */

	public interface ConexionInterfaz extends Remote {
	
	/**
	 * Compueba que el usuario y contraseña sean correctos y crea el objeto remoto Sesion asociado con el email que recibe como parámetro.
	 * Además se encarga de almacenar la dirección IPv4 del usuario para las funciones de callback.
	 * @param email
	 * @param pass
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean iniciarSesion(String email, String pass) throws RemoteException;
	/**
	 * Crea un nuevo objeto User con los parámetros recibidos y llama al método inciarSesion.
	 * @param email
	 * @param name
	 * @param apellidos
	 * @param password
	 * @param foto
	 * @param web
	 * @param publico
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean crearUsuario(String email, String name, String apellidos, String password, byte[] foto, String web, boolean publico) throws RemoteException;
	
}
