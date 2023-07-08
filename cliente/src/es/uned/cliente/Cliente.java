// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.GUI;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;

public class Cliente {
	static String URLGestor = "rmi://localhost:"+ 8888 +"/ServicioGestor";
	static String URLAutenticacion = "rmi://localhost:"+ 8888 +"/ServicioAutenticacion";
	static String URLCallback;
	static BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
	static String nombre;
	static String nick;
	static String contraseña;
	
	public static void main(String[] args) throws Exception {
		menuPrimero();
		System.out.println("¡Hasta pronto! \n");
		System.exit(0);
	}
	
	/**
	 * Muestra por pantalla el primer menu de acceso del usuario hasta que seleccione salir
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public static void menuPrimero() throws IOException, NotBoundException{
		int seleccion1;
		do {seleccion1 = GUI.despliegaMenu(new String[]{"Registrar un nuevo usuario.", "Hacer login.", "Salir."});
		
			switch (seleccion1) {
				case 1: registrar();
				        break;
				case 2: logear();
				        break;
				case 3: break;
				
				}
			} while (seleccion1!=3);
	} 
	/**
	 * Muestra por pantalla el segundo menu de acciones del usuario hasta que seleccione hacer logout
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public static void menuSegundo() throws IOException, NotBoundException {
		int seleccion2;
		do {seleccion2 = GUI.despliegaMenu(new String[] {"Información del usuario.","Enviar Trino.","Listar Usuarios del Sistema.","Seguir a.","Dejar de seguir a.","Salir (Logout)"});
		
			switch (seleccion2) {
			case 1: informar();
			        break;
			case 2: trinarCliente();
			        break;
			case 3: listar();
			        break;
			case 4: seguir();
			        break;
			case 5: noSeguir();
			        break;
			case 6: deslogear();
			        break;
			        
			
			}
		} while (seleccion2!=6);
		//Llamamos a método remoto
		ServicioAutenticacionInterface aut = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticacion);
		aut.logout(nick);
	}
	/**
	 * Registra un nuevo usuario en el sistema pidiendo su nombre, nick y contraseña
	 * @throws IOException
	 * @throws NotBoundException
	 */
	private static void registrar() throws IOException, NotBoundException {
		System.out.println("Introduzca su Nombre: \n");
		nombre = obj.readLine();
		System.out.println("Introduzca su nick: \n");
		nick = obj.readLine();
		System.out.println("Introduzca su contraseña: \n");
		contraseña = obj.readLine();
		//Llamamos a método remoto
		ServicioAutenticacionInterface aut = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticacion);
		boolean registrado = aut.registrar(nombre,nick,contraseña);
		if (!registrado){System.out.println("Nick ya existe, pruebe con otro nick \n");} 
		else {System.out.println("Registrado con éxito \n");
		}
	}
	/**
	 * Logea a un usuario existente en el sistema
	 * @throws IOException
	 * @throws NotBoundException
	 */
	private static void logear() throws IOException, NotBoundException {
		boolean autenticado;
		System.out.println("Para autenticarse introduzca su nick: \n");
		nick = obj.readLine();
		System.out.println("Introduzca su contraseña: \n");
		contraseña = obj.readLine();
		//Creamos objeto remoto callback asociado al nuevo usuario
		CallbackUsuarioInterface callback = new CallbackUsuarioImpl();
		//Lo nombramos con un identificador único asociado al objeto remoto
		int identificador =  System.identityHashCode(callback);
		URLCallback = "rmi://localhost:" + 8888 + "/ServicioCallback/" + identificador;
		Naming.rebind(URLCallback, callback);
		//Llamamos a método remoto
		ServicioAutenticacionInterface aut = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticacion);
		autenticado = aut.autenticar(nick,contraseña, callback);
		if (!autenticado) {System.out.println("Nick o contraseña incorrectos \n");obj.readLine();}
		else{ System.out.println("Autenticado con éxito");menuSegundo();}
		
	}
	/**
	 * Muestra en la consola información relevante del usuario logeado
	 * @throws NotBoundException
	 * @throws IOException
	 */
	private static void informar() throws NotBoundException, IOException {
		System.out.println("Servicio de gestión de Callbacks activo: \n");
		System.out.println(URLCallback);
		System.out.println("Información del usuario: \n");
		ServicioGestorInterface ges = (ServicioGestorInterface) Naming.lookup(URLGestor);
		ArrayList<String> lista = ges.info(nick, contraseña);
		GUI.imprimeArray(lista);
	}
	/**
	 * permite al usuario publicar un trino que será recibido por sus seguidores
	 * @throws IOException
	 * @throws NotBoundException
	 */
    private static void trinarCliente() throws IOException, NotBoundException {
    	ServicioGestorInterface ges = (ServicioGestorInterface) Naming.lookup(URLGestor);
    	System.out.println("Escriba su trino:\n");
    	String mensaje = obj.readLine();
    	Trino trino = new Trino(nick,mensaje);
		ges.enviarTrino(nick, trino);
	}
    /**
     * Lista por pantalla todos los usuarios registrados en el sistema
     * @throws NotBoundException
     * @throws IOException
     */
    private static void listar() throws NotBoundException, IOException {
    	ServicioGestorInterface ges = (ServicioGestorInterface) Naming.lookup(URLGestor);
    	ArrayList<String> lista = ges.listar();
    	GUI.imprimeArray(lista);
	}
    /**
     * Permite al usuario logeado seguir a otro usuario registrado en el sistema
     * @throws IOException
     * @throws NotBoundException
     */
    private static void seguir() throws IOException, NotBoundException {
		System.out.println("Introduzca el apodo del usuario al que va a seguir: \n");
		String apodo = obj.readLine();
		ServicioGestorInterface ges = (ServicioGestorInterface) Naming.lookup(URLGestor);
		boolean resultado = ges.seguir(nick, apodo);
		if (resultado) {System.out.println("Usuario " + apodo + " seguido con éxito. \n");}
		else {System.out.println("Imposible realizar la acción, consulte su lista de usuarios seguidos y la lista de usuarios disponibles \n");}
	}
    /**
     * Permite al usuario logeado dejar de seguir a otro usuario registrado en el sistema
     * @throws IOException
     * @throws NotBoundException
     */
    private static void noSeguir() throws IOException, NotBoundException {
    	System.out.println("Introduzca el apodo del usuario al que va a dejar de seguir: \n");
		String apodo = obj.readLine();
		ServicioGestorInterface ges = (ServicioGestorInterface) Naming.lookup(URLGestor);
		boolean resultado = ges.noSeguir(nick, apodo);
		if (resultado) {System.out.println("Operación realizada con éxito. \n");}
		else {System.out.println("Imposible realizar la acción, consulte su lista de usuarios seguidos. \n");}
	}
    
    /**
     * Permite al usuario cerrar su sesión en el sistema
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    private static void deslogear() throws RemoteException, MalformedURLException, NotBoundException {
    	//Llamamos a método remoto
    	ServicioAutenticacionInterface aut = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticacion);
    	aut.logout(nick);
    	Naming.unbind(URLCallback);
    }
    
}
