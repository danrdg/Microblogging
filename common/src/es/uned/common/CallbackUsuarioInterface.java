// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackUsuarioInterface extends Remote{

	/**
	 * Imprime por pantalla el nick del propietario del trino y su mensaje
	 * @param trino a imprimir
	 */
	public void recibeTrino (Trino tri) throws RemoteException;
}
