package es.studium.Ejercicio12;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Ejercicio12
{

	public static void main(String[] args)
	{

		try
		{
			Scanner teclado = new Scanner(System.in);
			FileOutputStream fos = new FileOutputStream("uno.bin"); // crear fichero
			BufferedOutputStream bos = new BufferedOutputStream (fos);
			//Necesitamos un objeto de la clase DataOutputStream
			DataOutputStream salidaB = new DataOutputStream(bos); // Escribir en el buffer
			//Meter un double en el archivo
			int dato;
			System.out.println("Dame un numero entero ");
			dato = teclado.nextInt();
			
			salidaB.writeInt(dato); // guardar dato decimal. por eso double writeint si seria un entero
			salidaB.close();// cerrar todo 
			bos.close();
			fos.close();
			System.out.println("¡Archivo creado correctamente!");
		}
		catch(IOException i)
		{
			System.out.println("Se produjo un error de Archivo");
		}
	}

}
 /* Otra manera de hacerlo.
  *  int dato;
                        try
                        {
                                    FileOutputStream fos = new FileOutputStream ("uno.bin");
                                    BufferedOutputStream bos = new BufferedOutputStream (fos);
                                    //Necesitamos un objeto de la clase DataOutputStream
                                    DataOutputStream salidaB = new DataOutputStream(bos);
                                    BufferedReader in = new BufferedReader ( new InputStreamReader ( System.in ) ) ; (LEER POR TECLADO)
                                    System.out.println("Introduce un número entero:");
                                    dato = Integer.parseInt(in.readLine());
                                    salidaB.writeInt(dato);
                                    salidaB.close();
bos.close();
fos.close();
                                    System.out.println("Archivo creado correctamente!");
                        }
                        catch(IOException i)
                        {
                                    System.out.println("Se produjo un error de Archivo");
                        }
            }
}
*/
