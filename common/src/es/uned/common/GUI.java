// Autor: Daniel Rodriguez gonzalez
// Correo: drodrigue1524@alumno.uned.es
package es.uned.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GUI {

public GUI () {}

/**
 * Despliega un menú aceptando como entrada las secciones del mismo
 * @param secciones en formato String
 * @return devuelve la opción seleccionada por teclado en formato int
 * @throws IOException
 */
public static int despliegaMenu(String[] secciones) throws IOException {
	 System.out.println("Elija una opción:\n");
	 
	 for (int i=0;i<secciones.length;i++) {
		 System.out.println((1+i) + ".-" + secciones[i]);
	 }
	 BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
	 String str;
	 boolean preguntar = true;
	 int opcion = 0;
	 do {
		 str = obj.readLine();
		 preguntar=false;
		 if (esEntero(str)) {
			 opcion=Integer.parseInt(str);
			 if (opcion>secciones.length || opcion<=0) {
				 System.out.println("Introduzca una opción válida:\n");
				 preguntar=true;
			 }
		 }else {System.out.println("Introduzca una opción válida:\n");
			       preguntar=true;}
		 } while(preguntar);
	 return opcion;
	 }
	/**
	 * Comprueba si una cadena se puede convertir a entero
	 * @param str
	 * @return true si es posible, false si no lo es
	 */
	public static boolean esEntero(String str) {
    try {
        Integer.parseInt(str);
        return true;
    	}   
    catch(NumberFormatException e) {
        return false;
    	}
	}
	/**
	 * Dado un ArrayList de Strings lo imprime por pantalla
	 * @param ArrayList de strings
	 */
	public static void imprimeArray (ArrayList<String> array) {
		for (int i=0; i<array.size();i++) {
			System.out.println(array.get(i) + "\n");
		}
		
	}
}

