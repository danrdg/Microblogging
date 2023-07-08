// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface ServicioGestorInterface extends Remote{
	
	/**
	 * Devuelve información del usuario invocante
	 * @param nick y contraseña del usuario
	 * @return ArrayList con información relevante del usuario
	 */
	public ArrayList<String> info (String nick, String passw) throws RemoteException, MalformedURLException, NotBoundException;
	/**
	 * Hace que el nick seguidor siga al seguido, devuelve si ha tenido éxito
	 * @param nicks del seguidor y el seguido
	 * return true si ha tenido éxito, false si no es el caso
	 */
	public boolean seguir (String seguidor, String seguido) throws RemoteException, MalformedURLException, NotBoundException;
	/**
	 * Hace que el nick seguidor deje de seguir al seguido, devuelve si ha tenido éxito
	 * @param nicks del seguidor y el seguido
	 * return true si ha tenido éxito, false si no es el caso
	 */
	public boolean noSeguir (String seguidor, String seguido) throws RemoteException, MalformedURLException, NotBoundException;
	/**
	 * Cliente identificado por su nick envia trino
	 * @param nick del cliente y trino a enviar
	 */
	public void enviarTrino (String nick, Trino trino) throws RemoteException, MalformedURLException, NotBoundException;
	// Devuelve el nick de todos los usuarios del sistema
	public ArrayList<String> listar () throws RemoteException, MalformedURLException, NotBoundException;


   
}
