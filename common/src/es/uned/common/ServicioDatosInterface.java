// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface ServicioDatosInterface extends Remote {
	
	/**
	 * Crea un nuevo usuario en la base de datos
	 * @param nombre, nick y contraseña del usuairo a crear
	 * @return true si se crea con éxito, false en caso contrario
	 */
	public boolean creaUsuario (String nombre, String nick, String password) throws RemoteException;
	
	/**
	 * Añade usuario a la lista de usuarios logeados
	 * @param nombre, contraseña e interfaz de callback del usuario
	 * @return true si se hace con éxito, false en caso contrario
	 */
	public boolean logeaUsuario (String nick, String contraseña, CallbackUsuarioInterface call) throws RemoteException;
	
	/**
	 * Añade usuario a la lista de usuarios bloqueados
	 * @param nick del usuario a bloquear
	 * @return true si tiene éxito, false si noe s el caso
	 */
	public boolean bloqueaUsuario (String nick) throws RemoteException;
	
	/**
	 * Elimina usuario de la lista de usuarios logeados así como su interfaz de callback de la base de datos
	 * @param nick del usuario a bloquear
	 * @return true si tiene éxito, false si noe s el caso
	 */
	public void logoutUsuario (String nick) throws RemoteException;
	
	/**
	 * Elimina usuario de la lista de usuarios bloqueados
	 * @param nick del usuario a desbloquear
	 * @return true si tiene éxito, false si no es el caso
	 */
	public boolean desbloqueaUsuario (String nick) throws RemoteException;
	
	/**
	 * Devuelve contraseña del usuario dado su nick
	 * @param nick del usuario
	 * @return contraseña del usuario en formato String
	 */
	public String daContraseña (String nick) throws RemoteException;
	
	/**
	 * Devuelve nombre del usuario dado su nick
	 * @param nick del usuario
	 * @return nombre del usuario en formato String
	 */
	
	public String daNombre (String nick) throws RemoteException;
	
	/**
	 * Devuelve ArrayList de nicks de seguidores de un usuario
	 * @param nick del usuario
	 * @return ArrayList de nicks de seguidores del usuario
	 */
	public ArrayList<String> daSeguidores(String nick)throws RemoteException;
	
	
	/**
	 * Devuelve ArrayList de nicks de seguidos de un usuario
	 * @param nick del usuario
	 * @return ArrayList de nicks de seguidos del usuario
	 */
	public ArrayList<String> daSeguidos(String nick)throws RemoteException;
	
	/**
	 * Comprueba si el nick ya está registrado en el sistema
	 * @param nick del usuario a comprobar
	 * @return true si el usuario está registrado en el sistema, false en caso contrario
	 */
	public boolean compruebaNick(String seguido)throws RemoteException;
	
	/**
	 * Añade seguidor por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguidor a añadir a la cuenta del usuario
	 */
	public void añadeSeguidor (String nick, String seguidor) throws RemoteException;
	
	/**
	 * Añade seguido por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguido a añadir a la cuenta del usuario
	 */
	public void añadeSeguido (String nick, String seguido) throws RemoteException;
	
	/**
	 * Elimina seguidor por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguidor a eliminar a la cuenta del usuario
	 */
	public void eliminaSeguidor (String nick, String seguidor) throws RemoteException;
		
	/**
	 * Elimina seguido por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguido a eliminar a la cuenta del usuario
	 */
	public void eliminaSeguido (String nick, String seguido) throws RemoteException;
	
	/**
	 * Devuelve si un usuario está o no conectado según su nick
	 * @param nick del usuario a comprobar
	 * @return true si el usuario está conectado, false si no es así
	 */
	public boolean siConectado (String nick) throws RemoteException;
	/**
	 * Devuelve si un usuario está o no bloqueado según su nick
	 * @param nick del usuario a comprobar
	 * @return true si el usuario está bloqueado, false si no es así
	 */
	public boolean siBloqueado (String nick) throws RemoteException;
	
	/**
	 * Devuelve Arraylist de nicks de usuarios registrados en el sistema
	 * @return ArrayList de nicks de usuarios registrados
	 */
	public ArrayList<String> daLista () throws RemoteException;

	/**
	 * Envia trino a interfaz para callback
	 * @param trino a enviar y nick del usuario que lo recibe
	 */
	public void trinar (Trino trino, String nick) throws RemoteException;
	
	
	/**
	 * Almacena trino en buzón de trinos pendientes del usuario
	 * @param trino a almacenar y usuario no logeado
	 */
	public void almacenar (Trino trino, String nick) throws RemoteException;
	/**
	 * Almacena trino publicado en la base de datos
	 * @param trino a archivar
	 */
	public void archivar(Trino trino) throws RemoteException;
	
	
	/**
	 * Devuelve ArrayList con trinos publicados
	 * @return ArrayList de trinos publicados
	 */
	public ArrayList<Trino> darTrinos() throws RemoteException;
	
	/**
	 * Almacena trino publicado por un usuario bloqueado
	 * @param nick del usuario bloqueado y el trino pendiente
	 */
	public void guardaBloq(String nick, Trino trino) throws RemoteException;
	
	/**
	 * Indica si un usuario bloqueado tiene trinos pendientes de publicar
	 * @param nick del usuario bloqueado
	 * @return true si hay trinos pendientes, false si no es así
	 */
	public boolean pendientes (String nick) throws RemoteException;
	
	/**
	 * Devuelve ArrayList de trinos pendientes de publicar de un usuario bloqueado y los borra de la base de datos
	 * @param nick del usuario bloqueado
	 * @return ArrayList trinos pendientes de publicar por baneo de usuario
	 */
	public ArrayList<Trino> darPendientes (String nick) throws RemoteException;
}
