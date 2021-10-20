package es.studium.FicheroBinarioEscritura;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FicheroBinarioEscritura
{
	public static void main(String[] args)
	{

		try
		{
			FileOutputStream fos = new FileOutputStream("Datos.dat"); // crear fichero
			BufferedOutputStream bos = new BufferedOutputStream (fos);
			//Necesitamos un objeto de la clase DataOutputStream
			DataOutputStream salidaB = new DataOutputStream(bos); // Escribir en el buffer
			//Meter un double en el archivo
			Double dato=7.345;
			salidaB.writeDouble(dato); // guardar dato decimal. por eso double writeint si seria un entero
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
