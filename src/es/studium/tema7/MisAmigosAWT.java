package es.studium.tema7;

//Todos los import
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Cración de la clase y los implementadores para las acciones
public class MisAmigosAWT  implements WindowListener, ActionListener
{
	// Atributos, objetos, variables:
	//-------------------------------

	//Constructor para la Creación del frame 
	//(ventana) en la que se mostrará todo
	Frame ventana = new Frame("MisAmigos");

	//Constructor para las Cajas de texto: 
	//------------------------------------
	//para el identificador del amigo
	TextField txtIdAmigo = new TextField(20);
	//para el nombre del amigo
	TextField txtNombreAmigo = new TextField(20);
	//para el teléfono del amigo
	TextField txtTelefonoAmigo = new TextField(20);

	//Constructor para los botones
	//-----------------------------

	//Botón para siguiente. Se le puede poner el nombre que se desee entre los paréntesis
	Button btnSiguiente = new Button(">>");
	//Botón para primero. Se le puede poner el nombre que se desee entre los paréntesis
	Button btnPrimero = new Button(">");
	//Botón para último. Se le puede poner el nombre que se desee entre los paréntesis
	Button btnUltimo = new Button("<");
	//Botón para anterior. Se le puede poner el nombre que se desee entre los paréntesis
	Button btnAnterior = new Button("<<");

	//Todo lo necesario para poder conectarse a MySQL desde Eclipse
	//--------------------------------------------------------------

	//Variable de tipo String que llamamos "driver" es con lo que nos conectamos al servidor MySQL
	String driver = "com.mysql.cj.jdbc.Driver";

	//Variable de tipo String que llamamos "url" es la dirección de la base de datos.
	/*tiene puesto a continuación del nombre de la base de datos ?serverTimezone=UTC
	 * que sirve para evitar errores de conexión al servidor. Sobre todo si se hace desde 
	 * un dispositivo que esté fuera de nuestra red
	 */
	String url = "jdbc:mysql://localhost:3306/amigos?serverTimezone=UTC";

	//Variable de tipo String que llamamos "login" 
	//es nombre de usuario con el que conectamos a MySQL Server
	String login = "root";

	//Variable de tipo String que llamamos "password" 
	//es la clave del usuario con el que conectamos a MySQL Server. Puede ser diferente
	String password = "Studium2019";

	//Variable de tipo String que llamamos "sentencia" 
	//es la sentencia que ejecutaremos para acceder a lo que deseamos en la BD
	//en este caso accedemos a la tabla misamigos
	String sentencia = "SELECT * FROM misamigos";

	//Para toda conexión a MySQL Server desde Eclipse necesitamos tres instrucciones
	//Connection, Statement y ResultSet. Todas las igualamos a null 
	//y podemos dar el nombre que deseemos en lugar de: connection, statement y rs
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//Creando la clase
	public MisAmigosAWT()
	{
		//Incluyendo el layout al Frame que llamamos ventana
		ventana.setLayout(new FlowLayout());

		//Para que no se pueda escribir o borrar lo que mostraran 
		//las cajas de texto ponemos el setEditable a false
		txtIdAmigo.setEditable(false);
		txtNombreAmigo.setEditable(false);
		txtTelefonoAmigo.setEditable(false);

		//Añadimos antes de incluir los textField el "try" con sus catch 
		//Probando si puede
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);

			//Establecer la conexión con la BD Empresa
			connection = DriverManager.getConnection(url, login, password);

			//Crear una sentencia que pueda ir hacia delante y atrás
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);

			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);

			//Para que muestre el sigyuiente al que tenemos en la cabecera inicial
			//Que se llama bor. Ahí es donde se inicia la presentación de la BD
			//el siguiente (next) será el primero de la lista de la tabla
			rs.next();

			//Para que muestre en cada caja de texto lo que hay en cada una de las filas de la tabla
			//--------------------------------------------------------------------------------------

			//Lo que tiene en el identificador del amigo
			txtIdAmigo.setText(rs.getInt("idAmigo")+"");

			//Lo que tiene en el nombre del amigo
			txtNombreAmigo.setText(rs.getString("nombreAmigo"));

			//Lo que tiene en el teléfono del amigo
			txtTelefonoAmigo.setText(rs.getInt("telefonoAmigo")+"");

		}
		//El catch en caso que haya error (lo llamamos cnfe iniciales de ClassNotFoundException)
		//En el caso que no se localice la clase que se busca o alguno de los elementos
		catch (ClassNotFoundException cnfe)
		{
			//Nos muestra por consola el texto Error 1: y el mensaje que se produzca
			System.out.println("Error 1-"+cnfe.getMessage());
		}
		//El catch en caso que haya error (lo llamamos sqle iniciales de SQLException)
		//En caso que haya un error en el servidor de SQL o en su acceso
		catch (SQLException sqle)
		{
			//Nos muestra por consola el texto Error 2: y el mensaje que se produzca
			System.out.println("Error 2-"+sqle.getMessage());
		}

		//Añadiendo más elementos al Frame:
		//---------------------------------

		//Añadiendo los textField. Cuidado con el orden
		ventana.add(txtIdAmigo);
		ventana.add(txtNombreAmigo);
		ventana.add(txtTelefonoAmigo);

		//Añadiendo los listener a los botones. 
		//Hay que hacerlo antes de añadirlos al Frame
		btnSiguiente.addActionListener(this);
		btnAnterior.addActionListener(this);
		btnPrimero.addActionListener(this);
		btnUltimo.addActionListener(this);

		//Añadiendo los botones. Cuidado con el orden
		ventana.add(btnPrimero);
		ventana.add(btnAnterior);
		ventana.add(btnSiguiente);
		ventana.add(btnUltimo);

		//Dando tamaño a la ventana (Frame)
		ventana.setSize(200,175);

		//Para que no se pueda redimensionar (reducir o agrandar)
		ventana.setResizable(false);

		//Para que aparezca en el centro de la pantalla
		ventana.setLocationRelativeTo(null);

		//Añadiendo el listener a la ventana
		ventana.addWindowListener(this);

		//Finalizamos haciendo visible la ventana
		ventana.setVisible(true);
	}

	//El método main con el que se crea la clase
	public static void main(String[] args)
	{
		new MisAmigosAWT();
	}

	//Declarando el evento para cerrar con el aspa de la ventana
	public void windowClosing(WindowEvent we)
	{
		//Ponemos otra excepción de try para los errores
		//y que nos permita cerrar la ventana sin que nos de problemas
		try
		{
			//En el supuesto que la conexión no sea posible
			if(connection!=null)
			{
				//Que la cierre si no es posible
				connection.close();
			}
		}
		//El catch para el error del servidor de MySQL. La llamamos "error"
		catch (SQLException error)
		{
			//Que nos muestre por pantalla el texto Error 3: y el mensaje correspondiente
			System.out.println("Error 3-"+error.getMessage());
		}
		//Instrucción para cerrar en el aspa
		System.exit(0);
	}

	//Agrupo todos los eventos de ventana que quedarán vacios
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	//Declarando el action event: 
	//Todo lo que van ha hacer los botones y las cajas de texto
	//Llamamos "evento" a la acción
	public void actionPerformed(ActionEvent evento)
	{
		//Si el evento corresponde al botón de siguiente
		if(evento.getSource().equals(btnSiguiente))
		{
			//Que intente hacerlo
			try
			{
				//Si hay algo después
				if(rs.next())
				{
					//Que muestre el siguiente dato de la tabla de la BD
					//Cada dato en su caja de texto
					txtIdAmigo.setText(rs.getInt("idAmigo")+"");
					txtNombreAmigo.setText(rs.getString("nombreAmigo"));
					txtTelefonoAmigo.setText(rs.getInt("telefonoAmigo")+"");
				}
				//De lo contrario
				else
				{
					//Que se quede en el último dato
					rs.last();
				}
			} 
			//En caso que surja un error en MySQL Server lo colocamos en "e"
			catch (SQLException e)
			{
				//Mostramos el error
				e.printStackTrace();
			}
		}
		//Si es otro botón el que recibe el evento (En este caso elde anterior)
		else if(evento.getSource().equals(btnAnterior))
		{
			//Se repiten todas las instrucciones del otro botón
			try
			{
				//Pero cambia que en lugar de si hay algo después
				//Ahora preguntamos si hay algo antes
				if(rs.previous())
				{
					//Que se muestre cada dato donde corresponde
					txtIdAmigo.setText(rs.getInt("idAmigo")+"");
					txtNombreAmigo.setText(rs.getString("nombreAmigo"));
					txtTelefonoAmigo.setText(rs.getInt("telefonoAmigo")+"");
				}
				//De lo contrario
				else
				{
					//Que se quede en el primero
					rs.first();
				}
			} 
			//En caso que surja un error en MySQL Server lo colocamos en "e"
			catch (SQLException e)
			{
				//Mostramos el error
				e.printStackTrace();
			}
		}
		//Si es otro botón el que recibe el evento (En este caso el de primero)
		else if(evento.getSource().equals(btnPrimero))
		{
			try
			{
				//Si es el primer dato
				if(rs.first())
				{
					//Que lo muestre en cada caja de texto
					txtIdAmigo.setText(rs.getInt("idAmigo")+"");
					txtNombreAmigo.setText(rs.getString("nombreAmigo"));
					txtTelefonoAmigo.setText(rs.getInt("telefonoAmigo")+"");
				}
			} 
			//En caso que surja un error en MySQL Server lo colocamos en "e"
			catch (SQLException e)
			{
				//Mostramos el error
				e.printStackTrace();
			}
		}
		//Si es otro botón el que recibe el evento (En este caso el de último)
		else if(evento.getSource().equals(btnUltimo))
		{
			try
			{
				//Si resulta que es el último dato
				if(rs.last())
				{
					//Que lo muestre en cada caja de texto
					txtIdAmigo.setText(rs.getInt("idAmigo")+"");
					txtNombreAmigo.setText(rs.getString("nombreAmigo"));
					txtTelefonoAmigo.setText(rs.getInt("telefonoAmigo")+"");
				}
			} 
			//En caso que surja un error en MySQL Server lo colocamos en "e"
			catch (SQLException e)
			{
				//Mostramos el error
				e.printStackTrace();
			}
		}
	}
}