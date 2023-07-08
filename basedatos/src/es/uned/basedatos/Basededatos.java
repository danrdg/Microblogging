// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.basedatos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import es.uned.common.GUI;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.Trino;





public class Basededatos {
	
	static final String URLDatos = "rmi://localhost:"+ 8888 +"/ServicioDatos";
	
	public static void main (String args[]) throws RemoteException, MalformedURLException, NotBoundException {
		try {
		int seleccion;
		//Lanzamos registro con numero de puerto 8888
		LocateRegistry.createRegistry(8888);
		//Creamos objeto remoto
		ServicioDatosImpl objExportado = new ServicioDatosImpl();
		
		//Nombramos objeto remoto
		Naming.rebind(URLDatos, objExportado);
		//Mostramos menu principal
		do {seleccion = GUI.despliegaMenu(new String[]{"Información de la base de datos.", "Listar Trinos (solo nick del propietario y el timestamp).", "Salir."});
		
		switch (seleccion) {
			case 1: informar();
			        break;
			case 2: trinos();
			        break;
			case 3: break;
			
			}
		} while (seleccion!=3); 
		}
		catch (Exception excr) {
			System.out.println("Excepcion en basedatos.main: " + excr);
		}
		Naming.unbind(URLDatos);
		System.exit(0);
	}
	
	/**
	 * Muestra por consola información acerca de la base de datos
	 */
	private static void informar() {
		System.out.println("Servicio de gestión de datos activo:\n");
		System.out.println(URLDatos + "\n");
	}
	/**
	 * Muestra por consola trinos publicados en el sistema
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private static void trinos() throws MalformedURLException, RemoteException, NotBoundException {
		ServicioDatosInterface dat = (ServicioDatosInterface) Naming.lookup(URLDatos);
		ArrayList<Trino> lista = dat.darTrinos();
		if (lista.isEmpty()) {
			System.out.println("No hay trinos puiblicados. \n");
		}else {
			System.out.println("Trinos publicados: \n");
			for (int i=0;i<lista.size();i++) {
				System.out.println(lista.get(i).GetNickPropietario() + "-" + lista.get(i).GetTimestamp() + "\n");
			}
		}
	}
	
	
}

