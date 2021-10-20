package es.studium.FicheroBinarioLectura;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FicheroBinarioLectura
{

	public static void main(String[] args)
	{
		try {
			FileInputStream fis = new FileInputStream ("Datos.dat");
			BufferedInputStream bis = new BufferedInputStream (fis);
			//Necesitamos un objeto de la clase DataInputStream
			DataInputStream entradaB = new DataInputStream(bis);
			//Sacar un double del archivo.
			Double numero; // mayuscula por que le da mas potencia
			numero= entradaB.readDouble();
			System.out.println(numero);

			entradaB.close();
			bis.close();
			fis.close();

		}
		catch(FileNotFoundException e)
		{
			System.out.println("Archivo NO encontrado");
		}
		catch(IOException i)
		{
			System.out.println("Se produjo un error de Archivo");
		}
	}
}
