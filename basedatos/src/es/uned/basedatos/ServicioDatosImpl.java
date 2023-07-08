// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es

package es.uned.basedatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.Trino;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface {
	//Usuarios conectados
	private ArrayList<String> conectados = new ArrayList<>();
	//Usuarios bloqueados
	private ArrayList<String> bloqueados = new ArrayList<>();
	//Trinos publicados
	private ArrayList<Trino> trinos = new ArrayList<>();
	//Trinos pendientes de publicar según el usuario bloqueado
	private Map <String, List<Trino>> bloqueado_trinos = new HashMap<String, List<Trino>>();
	//Buzón de trinos pendientes de mostrar a usuarios no logeados
	private Map <String, List<Trino>> buzon = new HashMap<String, List <Trino>>();
	//Contraseñas de cada usuario
	private Map <String, String> nick_contraseña = new HashMap<String, String>();
	//Nombres de cada usuario
	private Map <String, String> nick_nombre = new HashMap<String, String>();
	//Seguidores de cada usuario
	private Map <String,List<String>> nick_seguidores = new HashMap<String,List<String>>();
	//Seguidos de cada usuario
	private Map <String,List<String>> nick_seguidos = new HashMap<String,List<String>>();
	//Relaciona cada usuario con su interfaz de callback
	private Map <String,CallbackUsuarioInterface> nick_interface = new HashMap<String,CallbackUsuarioInterface>();
	

	
	public ServicioDatosImpl() throws RemoteException{
		super();
		
	//Creamos usuario registrado
		nick_contraseña.put("dan", "1234");
		nick_nombre.put("dan", "Daniel");
		ArrayList<String> array1 = new ArrayList<>();
		ArrayList<String> array2 = new ArrayList<>();
		nick_seguidores.put("dan", array1);
		nick_seguidos.put("dan", array2);
	}
	
	/**
	 * Crea un nuevo usuario en la base de datos
	 * @param nombre, nick y contraseña del usuairo a crear
	 * @return true si se crea con éxito, false en caso contrario
	 */
	
	public boolean creaUsuario (String nom,String nic,String passw) {
		if (nick_contraseña.containsKey(nic)){
			return false;
		} else {
			nick_contraseña.put(nic, passw);
			nick_nombre.put(nic,nom);
			ArrayList<String> array1 = new ArrayList<>();
			ArrayList<String> array2 = new ArrayList<>();
			nick_seguidores.put(nic, array1);
			nick_seguidos.put(nic, array2);
			return true;
		}
	}
	
	/**
	 * Añade usuario a la lista de usuarios logeados
	 * @param nombre, contraseña e interfaz de callback del usuario
	 * @return true si se hace con éxito, false en caso contrario
	 */
	
	public boolean logeaUsuario (String nick, String contraseña, CallbackUsuarioInterface call) throws RemoteException {
		if (nick_contraseña.containsKey(nick)) {
			String passw = nick_contraseña.get(nick);
			if (passw.equals(contraseña)){
				conectados.add(nick);
				nick_interface.put(nick, call);
				//Comprobamos trinos pendientes en el buzón y el usuario no está bloqueado
				if ((buzon.containsKey(nick))&& !(siBloqueado(nick))) {
					ArrayList<Trino>listaTrinos = (ArrayList<Trino>) buzon.get(nick);
					for (int i=0;i<listaTrinos.size();i++) {
						nick_interface.get(nick).recibeTrino(listaTrinos.get(i));
					}
					buzon.remove(nick);
				}
				return true;
			} else {return false;}
		} else {return false;}
		
	}
	
	/**
	 * Añade usuario a la lista de usuarios bloqueados
	 * @param nick del usuario a bloquear
	 * @return true si tiene éxito, false si no es el caso
	 */
	
	public boolean bloqueaUsuario (String nick)throws RemoteException {
		if (nick_contraseña.containsKey(nick)) {
			bloqueados.add(nick);
			return true;
		}
		else {return false;}
	}
	/**
	 * Elimina usuario de la lista de usuarios logeados así como su interfaz de callback de la base de datos
	 * @param nick del usuario a bloquear
	 */
	
	public void logoutUsuario (String nick)throws RemoteException {
		conectados.remove(nick);
		nick_interface.remove(nick);
	}
	
	/**
	 * Elimina usuario de la lista de usuarios bloqueados
	 * @param nick del usuario a desbloquear
	 * @return true si tiene éxito, false si no es el caso
	 */
	
	public boolean desbloqueaUsuario (String nick)throws RemoteException {
		if (nick_contraseña.containsKey(nick)) {
			bloqueados.remove(nick);
			return true;
		}
		else {return false;}
	}
	
	/**
	 * Devuelve contraseña del usuario dado su nick
	 * @param nick del usuario
	 * @return contraseña del usuario en formato String
	 */
	
	public String daContraseña (String nick)throws RemoteException {
		return nick_contraseña.get(nick);
	}
	
	/**
	 * Devuelve nombre del usuario dado su nick
	 * @param nick del usuario
	 * @return nombre del usuario en formato String
	 */
	
	public String daNombre (String nick)throws RemoteException {
		return nick_nombre.get(nick);
	}
	
	/**
	 * Devuelve ArrayList de nicks de seguidores de un usuario
	 * @param nick del usuario
	 * @return ArrayList de nicks de seguidores del usuario
	 */
	
	public ArrayList<String> daSeguidores (String nick) throws RemoteException{
		return (ArrayList<String>) nick_seguidores.get(nick);
	}
	
	/**
	 * Devuelve ArrayList de nicks de seguidos de un usuario
	 * @param nick del usuario
	 * @return ArrayList de nicks de seguidos del usuario
	 */
	
	public ArrayList<String> daSeguidos (String nick) throws RemoteException{
		return (ArrayList<String>) nick_seguidos.get(nick);
	}
	/**
	 * Comprueba si el nick ya está registrado en el sistema
	 * @param nick del usuario a comprobar
	 * @return true si el usuario está registrado en el sistema, false en caso contrario
	 */
	
	public boolean compruebaNick (String nick)throws RemoteException {
		if (nick_contraseña.containsKey(nick)) {
			return true;
		} else {return false;}
		}
	
	/**
	 * Añade seguidor por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguidor a añadir a la cuenta del usuario
	 */
	
	public void añadeSeguidor (String nick, String seguidor) throws RemoteException{
		ArrayList<String> a = (ArrayList<String>) nick_seguidores.get(nick);
		a.add(seguidor);
	}
	
	/**
	 * Añade seguido por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguido a añadir a la cuenta del usuario
	 */
	
	public void añadeSeguido (String nick, String seguido) throws RemoteException{
		ArrayList<String> a = (ArrayList<String>) nick_seguidos.get(nick);
		a.add(seguido);
		
	}
	/**
	 * Elimina seguidor por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguidor a eliminar a la cuenta del usuario
	 */
	
	public void eliminaSeguidor (String nick, String seguidor) throws RemoteException{
		ArrayList<String> a = (ArrayList<String>) nick_seguidores.get(nick);
		a.remove(seguidor);
	}
	/**
	 * Elimina seguido por su nick a la cuenta del nick referenciado
	 * @param nick del usuario y del seguido a eliminar a la cuenta del usuario
	 */
	
	public void eliminaSeguido (String nick, String seguido) throws RemoteException{
		ArrayList<String> a = (ArrayList<String>) nick_seguidos.get(nick);
		a.remove(seguido);
		
	}
	
	/**
	 * Devuelve si un usuario está o no conectado según su nick
	 * @param nick del usuario a comprobar
	 * @return true si el usuario está conectado, false si no es así
	 */
	public boolean siConectado (String nick) throws RemoteException{
		for (int i=0;i<conectados.size();i++) {
			if (conectados.get(i).equals(nick)) {return true;}
		}
		return false;
	}
	
	/**
	 * Devuelve si un usuario está o no bloqueado según su nick
	 * @param nick del usuario a comprobar
	 * @return true si el usuario está bloqueado, false si no es así
	 */
	public boolean siBloqueado (String nick) throws RemoteException{
		for (int i=0;i<bloqueados.size();i++) {
			if (bloqueados.get(i).equals(nick)) {return true;}
		}
		return false;
	}
	
	/**
	 * Devuelve Arraylist de nicks de usuarios registrados en el sistema
	 * @return ArrayList de nicks de usuarios registrados
	 */
	public ArrayList<String> daLista () throws RemoteException{
		ArrayList<String> a = new ArrayList<>();
		a.add("Lista de usuarios registrados en el sistema por apodo:");
		for (String str : nick_contraseña.keySet()) {
			  a.add(str);
			}
		return a;
	}

	
	/**
	 * Envia trino a interfaz para callback
	 * @param trino a enviar y nick del usuario que lo recibe
	 */
	public void trinar (Trino trino, String nick) throws RemoteException{
		nick_interface.get(nick).recibeTrino(trino);
	}
	
	/**
	 * Almacena trino en buzón de trinos pendientes del usuario
	 * @param trino a almacenar y usuario no logeado
	 */
	public void almacenar (Trino trino, String nick) throws RemoteException{
	if (buzon.containsKey(nick)) {
		buzon.get(nick).add(trino);
	}else {
		ArrayList<Trino>trinos = new ArrayList<>();
		buzon.put(nick, trinos);
		buzon.get(nick).add(trino);
	}
	}
	/**
	 * Almacena trino publicado en la base de datos
	 * @param trino a archivar
	 */
	public void archivar(Trino trino) throws RemoteException {
		trinos.add(trino);
	}
	/**
	 * Devuelve ArrayList con trinos publicados
	 * @return ArrayList de trinos publicados
	 */
	
	public ArrayList<Trino> darTrinos() throws RemoteException{
		return trinos;
	}
	
	/**
	 * Almacena trino publicado por un usuario bloqueado
	 * @param nick del usuario bloqueado y el trino pendiente
	 */
	public void guardaBloq(String nick, Trino trino) throws RemoteException{
		if (bloqueado_trinos.containsKey(nick)) {
			bloqueado_trinos.get(nick).add(trino);
		}else {
			ArrayList<Trino>trinos = new ArrayList<>();
			bloqueado_trinos.put(nick, trinos);
			bloqueado_trinos.get(nick).add(trino);
		}
	}
	
	/**
	 * Indica si un usuario bloqueado tiene trinos pendientes de publicar
	 * @param nick del usuario bloqueado
	 * @return true si hay trinos pendientes, false si no es así
	 */
	public boolean pendientes (String nick) throws RemoteException{
		if (bloqueado_trinos.containsKey(nick)) {
			return true;
		}else {return false;}
	}
	
	/**
	 * Devuelve ArrayList de trinos pendientes de publicar de un usuario bloqueado y los borra de la base de datos
	 * @param nick del usuario bloqueado
	 * @return ArrayList trinos pendientes de publicar por baneo de usuario
	 */
		public ArrayList<Trino> darPendientes (String nick) throws RemoteException{
			ArrayList<Trino> pendientes = (ArrayList<Trino>) bloqueado_trinos.get(nick);
			bloqueado_trinos.remove(nick);
			return pendientes;
		}
	
}
