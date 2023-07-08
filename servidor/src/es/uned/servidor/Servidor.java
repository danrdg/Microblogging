// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import es.uned.common.GUI;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;




public class Servidor {
	static final String URLGestor = "rmi://localhost:"+ 8888 +"/ServicioGestor";
	static final String URLAutenticacion = "rmi://localhost:"+ 8888 +"/ServicioAutenticacion";
	static final String URLDatos = "rmi://localhost:"+ 8888 +"/ServicioDatos";
	static BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
	public static void main (String args[]) throws RemoteException, MalformedURLException, NotBoundException {
		try {
		//Creamos objetos remotos
		ServicioAutenticacionImpl objExportado1 = new ServicioAutenticacionImpl();
		ServicioGestorImpl objExportado2 = new ServicioGestorImpl();
		//Nombramos objetos remotos
		Naming.rebind(URLGestor, objExportado2);
		Naming.rebind(URLAutenticacion, objExportado1);
		//Mostramos menú principal
		int seleccion1;
		do {seleccion1 = GUI.despliegaMenu(new String[]{"Información del servidor.", "Listar usuarios registrados.", "Listar usuarios logeados.","Bloquear (banear) usuario","Desbloquear usuario","Salir"});
		
		switch (seleccion1) {
			case 1: informar();
			        break;
			case 2: registrados();
			        break;
			case 3: logeados();
				    break;
			case 4: bloquear();
			        break;
			case 5: desbloquear();
			        break;
			case 6: break;
			
			}
		} while (seleccion1!=6);
	}
		catch (Exception excr) {
			System.out.println("Excepcion en Servidor.main: " + excr);
	}
		Naming.unbind(URLGestor);
		Naming.unbind(URLAutenticacion);
		System.exit(0);
	}
	/**
	 * Muestra por pantalla información del servidor
	 */
	private static void informar() {
		System.out.println("Servicio de Autenticacion activo:\n");
		System.out.println(URLAutenticacion + "\n");
		System.out.println("Servicio de Gestión activo:\n");
		System.out.println(URLGestor + "\n");
	}
	/**
	 * Muestra por pantalla usuarios registrados en el sistema
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private static void registrados() throws MalformedURLException, RemoteException, NotBoundException {
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		GUI.imprimeArray(dat.daLista());
	}
	/**
	 * Muestra por pantalla usuarios logeados en el sistema
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private static void logeados() throws MalformedURLException, RemoteException, NotBoundException {
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		ArrayList<String> todos = dat.daLista();
		ArrayList<String> logeados = new ArrayList<>();
		for (int i=0;i<todos.size();i++) {
			if (dat.siConectado(todos.get(i))) {
				logeados.add(todos.get(i));	
			}
		}
		if(logeados.isEmpty()) {
			System.out.println("Actualmente no hay usuarios logeados. \n");
		}else {
			System.out.println("Usuarios logeados:\n");
			GUI.imprimeArray(logeados);	
		}
		
	}
	/**
	 * Bloqea un usuario tras pedir su nick por pantalla
	 * @throws IOException
	 * @throws NotBoundException
	 */
	private static void bloquear() throws IOException, NotBoundException {
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		System.out.println("Introduzca el nick del usuario a bloquear:\n");
		String nick = obj.readLine();
		if (dat.bloqueaUsuario(nick)) {
			System.out.println("Usuario bloqueado con éxito. \n");
		}else {
			System.out.println("El nick introducido no existe. \n");
		}
	}
	/**
	 * Desbloquea a un usuario tras pedir su nick por pantalla
	 * @throws NotBoundException
	 * @throws IOException
	 */
	private static void desbloquear() throws NotBoundException, IOException {
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		ServicioGestorInterface ges = (ServicioGestorInterface) Naming.lookup(URLGestor);
		System.out.println("Introduzca el nick del usuario a desbloquear:\n");
		String nick = obj.readLine();
		if (dat.desbloqueaUsuario(nick)) {
			//Al desbloquear al usuario se publican todos sus trinos pendientes
			if (dat.pendientes(nick)) {
				ArrayList<Trino> pendientes = dat.darPendientes(nick);
				for (int i=0;i<pendientes.size();i++) {
					ges.enviarTrino(nick, pendientes.get(i));
				}	
			}
			System.out.println("Usuario desbloqueado con éxito. \n");
		}else {
			System.out.println("El nick introducido no existe. \n");
		}
	}
	
	
}
	

