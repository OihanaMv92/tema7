package es.studium.tema7;


//Todos los import correspondientes
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
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

//Implementamos Listeners
public class Login implements WindowListener, ActionListener
{
	// ESTOS SON LOS CONSTRUCTORES PARA TODA LA CLASE:
	// Creamos la ventana Frame Login
	Frame ventanaLogin = new Frame("Login");
	// Creamos la ventana Dialog en caso de introducir datos erróneos
	Dialog dialogoLogin = new Dialog(ventanaLogin, "Error", true); // true es modal, hasta que no lo cierre no podemos
	// volver a lo que está detrás

	// Las etiquetas que se leen dentro de la ventana y el diálogo
	Label lblUsuario = new Label("Usuario:");
	Label lblClave = new Label("Clave:");
	Label lblError = new Label("Credenciales Erróneas!!!");
	// Los cuadros de texto donde se ingresa usuario y password
	TextField txtUsuario = new TextField(20);
	TextField txtClave = new TextField(20);
	// Los botones para acceder o limpiar en los cuadros de textos
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");

	// --------------------------------------------------------------------------------------------------------------------
	// PARÁMETROS PARA CONECTARNOS A LA BD:
	// El driver para poder conectarse a MySQL Server
	String driver = "com.mysql.cj.jdbc.Driver";

	// La dirección de la base de datos para conectarnos a misoftware
	String url = "jdbc:mysql://localhost:3306/misoftware?serverTimezone=UTC";

	// El usuario con el que conectamos, en este caso ROOT
	// Este usuario no tiene nada que ver con los usuarios creados por
	// nosotros, este es un usuario de la BD de MYSQL
	String login = "root";

	// La contraseña de conexión (Puede variar de un usuario a otro)
	String password = "Studium2020;";

	// Declaración de una variable tipo Cadena en la que
	// pondremos las instrucciones o sentencias para MySQL Server
	String sentencia = "";

	// Elementos necesarios para realizar la conexión: Connection, Statement,
	// ResulSet
	// El método que se ha creado más abajo para la conexión (Mirarlo abajo) e
	// iniciamos en nulo
	Connection connection = null;

	// El Statement que lo creamos con ese nombre(lo que está azul) y lo ponemos
	// nulo
	Statement statement = null;

	// El ResultSet lo creamos con ese nombre y lo iniciamos en nulo y con esto
	// sacaremos la información de la BD
	ResultSet rs = null;

	// --------------------------------------------------------------------------------------------------------------------
	// Construcción de la clase con todos sus elementos
	public Login()
	{
		// El tipo de Layout de la ventana, en éste caso FlowLayout
		ventanaLogin.setLayout(new FlowLayout());

		// Añadiendo los elementos a la ventana:
		// Etiqueta y cuadro de texto para nombre de usuario
		ventanaLogin.add(lblUsuario);
		ventanaLogin.add(txtUsuario);
		// Etiqueta y cuadro de texto para nombre de clave
		ventanaLogin.add(lblClave);
		// para ocultar con * los caracteres introducidos en Clave
		txtClave.setEchoChar('*');
		ventanaLogin.add(txtClave);
		// Dar funcionalidad a los botones
		btnAceptar.addActionListener(this);
		ventanaLogin.add(btnAceptar);
		btnLimpiar.addActionListener(this);
		ventanaLogin.add(btnLimpiar);
		// Para poder cerrar ventana
		ventanaLogin.addWindowListener(this);

		ventanaLogin.setSize(260, 130);
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setResizable(false);
		ventanaLogin.setVisible(true);
	}

	public static void main(String[] args)
	{
		// Llamamos a nuestro constructor
		new Login();
	}

	@Override
	public void actionPerformed(ActionEvent botonPulsado)
	{
		// Diferencia al pulsar botón Limpiar y botón Aceptar
		if (botonPulsado.getSource().equals(btnLimpiar))
		{
			// Vaciar los txt
			txtUsuario.selectAll();
			txtUsuario.setText("");
			txtClave.selectAll();
			txtClave.setText("");
			txtUsuario.requestFocus();
			// Ponemos el foco aparecerá en Usuario siempre
			txtUsuario.requestFocus();
		} else if (botonPulsado.getSource().equals(btnAceptar))
		{
			// Conectamos con la Base de Datos
			connection = conectar();
			// Buscar lo que el usuario ha escrito en los TextField
			sentencia = "SELECT * FROM usuarios WHERE nombreUsuario='" + txtUsuario.getText()
			+ "' AND claveUsuario = SHA2('" + txtClave.getText() + "',256);";
			try
			{
				// Crear una sentencia para statement
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

				// Lanzamos un SELECT a la Base de Datos
				rs = statement.executeQuery(sentencia);
				if(rs.next()) // Si ha encontrado algo
				{
					// Si existe en Base de Datos, muestra el Menú Principal
				}
				else // Si no encuentra nada
				{
					// Si no existe en BD, muestra Diálogo de Error
					dialogoLogin.setLayout(new FlowLayout());
					dialogoLogin.add(lblError);
					dialogoLogin.addWindowListener(this);
					dialogoLogin.setSize(150,75);
					dialogoLogin.setLocationRelativeTo(null);
					dialogoLogin.setResizable(false);
					dialogoLogin.setVisible(true);
				}

			}
			// En caso de error en MySQL salta el mensaje de error
			catch (SQLException sqle)
			{
			}
			// Desconectar la BD
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		if(dialogoLogin.isActive())
		{
			dialogoLogin.setVisible(false);
		}
		else
		{
			System.exit(0);

		}

	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
	}

	public Connection conectar() // lo llamamos conectar
	{
		// Probando hacer
		try
		{
			// Cargar los controladores para el acceso a la BD
			// Diciendo la Clase con el nombre que pusimos al archivo .jar
			// el que tenemos que asociar para que se pueda conectar a la BD de MySQL Server
			Class.forName(driver); // Lo llamamos driver

			// Establecer la conexión con la BD Empresa
			// Con la dirección en la que se encuentra: url
			// El usuario con el que vamos a conectar: login
			// La clave para la conexión: password
			connection = DriverManager.getConnection(url, login, password);
		}
		// En caso de error de no localizar algo o
		// que no funcione algún dato de url, login o pass
		// Aparece el error 1 seguido del mensaje que se guarda en "cnfe"
		catch (ClassNotFoundException cnfe) // llamamos a la excepción cnfe
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		// En caso de error en MySQL salta el error 2
		// seguido del mensaje que se guarda en "cnfe"
		catch (SQLException sqle)
		{
			System.out.println("Error 2-" + sqle.getMessage());
		}
		// Lo que devuelve el método es connection
		// Precisamente es lo que hace cuando llamamos al método
		// Uno de los valores necesarios para la conexión era
		// connection que lo iniciamos en null
		return connection;
	}
}