// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote {
	
	/**
	 * Dados nombre, nick y contraseña, registra nuevo usuario en el sistema	
	 * @param nombre, nick y contraseña
	 * @return true si tiene éxito, false si no es el caso
	 */
	public boolean registrar (String nombre, String nick, String password) throws RemoteException;
	
	/**
	 * Dados nick y contraseña autentica usuario en el sistema
	 * @param nick, contraseña y interfaz de callback del usuario
	 * @return true si ha tenido éxito, false si no es el caso
	 */
	public boolean autenticar (String nick, String password,CallbackUsuarioInterface call) throws RemoteException;
	
	/**
	 * Cierra sesión en el sistema
	 * @param nick del usuario cuya sesión se cierra
	 */
	public void logout (String nick) throws RemoteException, MalformedURLException, NotBoundException;

}
