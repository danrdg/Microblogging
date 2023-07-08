// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;


public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{
	final String URLDatos = "rmi://localhost:"+ 8888 +"/ServicioDatos";
	
	public ServicioAutenticacionImpl() throws RemoteException{
		super();
	}
/**
 * Dados nombre, nick y contraseña, registra nuevo usuario en el sistema	
 * @param nombre, nick y contraseña
 * @return true si tiene éxito, false si no es el caso
 */
	public boolean registrar (String nombre, String nick, String password) throws RemoteException{
		try {
			ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
			return dat.creaUsuario(nombre,nick,password);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			
			e.printStackTrace();
			return false;
		}
	}
/**
 * Dados nick y contraseña autentica usuario en el sistema
 * @param nick, contraseña y interfaz de callback del usuario
 * @return true si ha tenido éxito, false si no es el caso
 */
	public boolean autenticar (String nick, String password, CallbackUsuarioInterface call) throws RemoteException{
		
		try {
			ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
			return dat.logeaUsuario(nick, password, call);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			return false;
		}
	}
/**
 * Cierra sesión en el sistema
 * @param nick del usuario cuya sesión se cierra
 */
		public void logout (String nick) throws RemoteException, MalformedURLException, NotBoundException{
			ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
			dat.logoutUsuario(nick);
	}
}
