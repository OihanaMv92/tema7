package es.studium.tema7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class misamigos
{

	public static void main(String[] args)
	{
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/amigos"; //por que esta en local si fuera de otro lado poner ip y nombre de la BD
		String login = "root"; //usuario
		String password = "Studium2020;"; //contraseña
		String sentencia = "SELECT * FROM misamigos"; // ver la tabla
		Connection connection = null; //permite conectarnos
		Statement statement = null; // ejecutar instrucion sql
		ResultSet rs = null; // trae la informacion de la BD
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			//Establecer la conexión con la BD Empresa
			connection = DriverManager.getConnection(url, login, password);
			//Crear una sentencia
			statement = connection.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				System.out.println(rs.getInt("idamigo") + "-" + rs.getString("nombreAmigo")+"-"+rs.getString("telefonoAmigo"));
			}
		}
		catch (ClassNotFoundException cnfe)
		{
			System.out.println("Error 1-"+cnfe.getMessage());
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		finally
		{
			try
			{
				if(connection!=null)
				{
					connection.close();
				}
			}
			catch (SQLException e)
			{
				System.out.println("Error 3-"+e.getMessage());
			}
		}
	}
}
