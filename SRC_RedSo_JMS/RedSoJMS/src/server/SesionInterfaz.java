package server;

import java.rmi.Remote;
import java.util.List;
import java.rmi.RemoteException;

/**
 * Interfaz remota que abstrae el concepto de sesi�n, permitiendo al cliente asociado a la instancia <br>
 * de esta interfaz realizar las funcionalidades b�sicas de este sistema tras la conexi�n.
 * @author V�ctor Portals Lorenzo
 * 
 *
 */
public interface SesionInterfaz extends Remote {
	
	/**
	 * Devuelve una lista iterable de mensaje ordenados por fecha tanto del usuario como de sus amigos.<br>
	 * @param email
	 * @return List<Message>
	 * @throws RemoteException
	 */
	public List<Message> obtenerNovedades(String email) throws RemoteException;
	/**
	 * Devuelve un array de Strings que contienen datos del usuario para conformar su muro. La mayor parte <br>
	 * de su funcionalidad a quedado sin uso ya que los mensajes se obtienen por otros medios.
	 * @param email
	 * @return String[]
	 * @throws RemoteException
	 */
	public String[] getDatos(String email) throws RemoteException;
	
	/**
	 * Devuelve un array de bytes que se corresponde con una foto (de perfil) en formato jpg.
	 * @param email
	 * @return byte[]
	 * @throws RemoteException
	 */
	public byte[] getFoto(String email) throws RemoteException;
	/**
	 * En caso que no existir crea un registro de amistad en tabla Friend entre el usuario que lo invoca y el usuario asociado<br>
	 * al email pasado como par�metro. Para no ralentizar el sistema crea un thread paralelo que se encarga de notificar <br>
	 * mediante callback al usuario a�adido como amigo sin ralentizar el sistema.
	 * @param email_2
	 * @throws RemoteException
	 */
	public void addFriend(String email_2) throws RemoteException;
	/**
	 * El usuario del programa cliente invocante confirma su conformidad con la petici�n de amistad del usuario<br>
	 * asociado al email pasado como par�metro. Se modifica la tabla Friend.
	 * @param email_1
	 * @throws RemoteException
	 */
	public void confirmFriend(String email_1) throws RemoteException;
	/**
	 * Se borra el registro de la tabla Friend que relaciona al usuario invocante y el usuario asociado al email pasado<br>
	 * como par�metro
	 * @param email_1
	 * @throws RemoteException
	 */
	public void refuseFriend(String email_1) throws RemoteException;
	/**
	 * Devuelve un array de booleanos que contiene informaci�n sobre el registro de dos usuarios en la tabla Friend. El array <br>
	 * devuelto tiene la siguiente estructura [Existe registro , amistad_confirmada , invocante_acepta , usuario_par�metro_acepta].
	 * @param email_objetivo
	 * @return boolean[]
	 * @throws RemoteException
	 */
	public boolean[] getFriendship(String email_objetivo) throws RemoteException;
	/**
	 * Se crea un registro en la tabla Message con los datos del usuario, el mensaje y la hora al momento de la invocaci�n.<br>
	 * Adem�s crea un thread paralelo encargado de notificar a las aplicaciones cliente mediante callback sin ralentizar el <br>
	 * sistema.
	 * @param mensaje
	 * @throws RemoteException
	 */
	public void publishMessage(String mensaje) throws RemoteException;
	/**
	 * Devuelve una lista iterable de objetos Message cuyo email_ID se corresponda con el pasado como par�metro.
	 * @param email
	 * @return List\<Message\>
	 * @throws RemoteException
	 */
	public List<Message> getMessages(String email) throws RemoteException;
	/**
	 * Devueve una lista iterable de objetos User que cumplan con el siguiente criterio de busqueda: <br>
	 * Se separa el String recibido como par�metro en palabras, y se compara cada una con el campo email y con el campo apellidos <br>
	 * en busca de coincidencias.
	 * @param busqueda
	 * @return List\<User\>
	 * @throws RemoteException
	 */
	public List<User> getResults(String busqueda) throws RemoteException; 
	/**
	 * Devueve una lista iterable de objetos User cuyo atributo email sea el campo email_2 donde el atributo email del <br>
	 * usuario invocante sea el campo email_1 en la tabla Friend o viceversa.
	 * en busca de coincidencias.
	 * @param email
	 * @return List\<User\>
	 * @throws RemoteException
	 */
	public List<User> getFriends(String email) throws RemoteException; 
	/**
	 * Elimina el objeto remoto asociado con un usuario que ha inicado sesi�n.
	 * @param email
	 * @throws RemoteException
	 */
	public void logOut(String email) throws RemoteException;
	
	public boolean getPublic(String email) throws RemoteException;
	
	public boolean modifyProfile(String nombre, String apellidos, String web, Boolean isPublic, byte[] foto) throws RemoteException;

}
