// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.Trino;

public class CallbackUsuarioImpl extends UnicastRemoteObject implements CallbackUsuarioInterface {

	
	protected CallbackUsuarioImpl() throws RemoteException {
		super();
		
	}
/**
 * Imprime por pantalla el nick del propietario del trino y su mensaje
 * @param trino a imprimir
 */
	public void recibeTrino(Trino tri) throws RemoteException {
		
		System.out.println(">" + tri.GetNickPropietario() + "#" + tri.GetTrino() + "\n");
	}

}
