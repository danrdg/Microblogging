// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {
	static final String URLDatos = "rmi://localhost:"+ 8888 +"/ServicioDatos";
	
	
	public ServicioGestorImpl() throws RemoteException{
		super();
	}
	/**
	 * Devuelve información del usuario invocante
	 * @param nick y contraseña del usuario
	 * @return ArrayList con información relevante del usuario
	 */
	public ArrayList<String> info (String nick, String passw) throws RemoteException, MalformedURLException, NotBoundException{
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		ArrayList<String> lista = new ArrayList<String>();
		ArrayList<String> seguidores = dat.daSeguidores(nick);
		ArrayList<String> seguidos = dat.daSeguidos(nick);
		lista.add("Nombre de Usuario:");
		lista.add(dat.daNombre(nick));
		lista.add("Apodo:");
		lista.add(nick);
		lista.add("Contraseña:");
		lista.add(passw);
		lista.add("Seguidores:");
		for (int i=0; i<seguidores.size();i++) {
			lista.add(seguidores.get(i));
		}
		lista.add("Seguidos:");
		for (int i=0; i<seguidos.size();i++) {
			lista.add(seguidos.get(i));
		}
		return lista;
		
		
	}
	/**
	 * Hace que el nick seguidor siga al seguido, devuelve si ha tenido éxito
	 * @param nicks del seguidor y el seguido
	 * return true si ha tenido éxito, false si no es el caso
	 */
	public boolean seguir (String seguidor, String seguido) throws RemoteException, MalformedURLException, NotBoundException{
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		//Comprueba que el usuario a seguir no sea él mismo
		if (seguidor.equals(seguido)) {return false;}
		//Comprueba si el usuario a seguir existe
		boolean condicion1 = dat.compruebaNick(seguido);
		boolean condicion2 = true;
		if (condicion1) {
			//Comprueba si el invocante está en la lista de seguidores del usuario a seguir
			ArrayList<String> seguidores = dat.daSeguidores(seguido);
			for (int i=0;i<seguidores.size();i++) {
				if (seguidores.get(i).equals(seguidor)) {condicion2=false;}
				}
		}
		if (condicion1 && condicion2) {
			dat.añadeSeguidor(seguido,seguidor);
			dat.añadeSeguido(seguidor, seguido);
			return true;
		}else {return false;}		
		
	}
	/**
	 * Hace que el nick seguidor deje de seguir al seguido, devuelve si ha tenido éxito
	 * @param nicks del seguidor y el seguido
	 * return true si ha tenido éxito, false si no es el caso
	 */
	public boolean noSeguir (String seguidor, String seguido) throws RemoteException, MalformedURLException, NotBoundException{
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		//Comprueba si el usuario a seguir existe
		boolean condicion1 = dat.compruebaNick(seguido);
		boolean condicion2 = false;
		if (condicion1) {
			//Comprueba si el invocante está en la lista de seguidores del usuario a seguir
			ArrayList<String> seguidores = dat.daSeguidores(seguido);
			for (int i=0;i<seguidores.size();i++) {
				if (seguidores.get(i).equals(seguidor)) {condicion2=true;}
				}
		}
		if (condicion2) {
			dat.eliminaSeguidor(seguido,seguidor);
			dat.eliminaSeguido(seguidor, seguido);
			return true;
		} else {return false;}
		
		
	}
	/**
	 * Cliente identificado por su nick envia trino
	 * @param nick del cliente y trino a enviar
	 */
	public void enviarTrino (String nick, Trino trino) throws RemoteException, MalformedURLException, NotBoundException{
	ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
	ArrayList<String> seguidores = dat.daSeguidores(nick);
	//Primero comprobamos si el usuario está bloqueado
	if (dat.siBloqueado(nick)) {
		//Si lo está almacenamos el trino a la espera de que el usuario sea desbloqueado
		dat.guardaBloq(nick, trino);
	}else {
		//Si no está bloqueado almacenamos la publicación
		dat.archivar(trino);
		for (int i=0;i<seguidores.size();i++) {
			// Si el seguidor está conectado y no bloqueado recibe el trino
			if ((dat.siConectado(seguidores.get(i))) && !(dat.siBloqueado(seguidores.get(i))))
				{
					//Lo mostramos por pantalla si el seguidor está conectado y no bloqueado
					dat.trinar(trino, seguidores.get(i));
				}else {
					//O lo almacenamos a la espera de que se conecte o el seguidor sea desbloqueado
					dat.almacenar(trino,seguidores.get(i));
				}
			}
		}
	}		
	
	/**
	 * Devuelve el nick de todos los usuarios del sistema
	 * @return ArrayList que contiene todos los nicks registrados en el sistema
	 */
	public ArrayList<String> listar () throws RemoteException, MalformedURLException, NotBoundException{
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		ArrayList<String> lista = dat.daLista();
		return lista;
	}
		
}
