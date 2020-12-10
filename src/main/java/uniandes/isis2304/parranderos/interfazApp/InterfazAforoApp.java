/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.AFOROCCANDES;
import uniandes.isis2304.parranderos.negocio.CARNET;
import uniandes.isis2304.parranderos.negocio.VOBAÑO;
import uniandes.isis2304.parranderos.negocio.VOCARNET;
import uniandes.isis2304.parranderos.negocio.VOESPACIO;
import uniandes.isis2304.parranderos.negocio.VOLECTOR;
import uniandes.isis2304.parranderos.negocio.VOLOCAL_COMERCIAL;
import uniandes.isis2304.parranderos.negocio.VOPARQUEADERO;
import uniandes.isis2304.parranderos.negocio.VOVISITA;
import uniandes.isis2304.parranderos.negocio.VOVISITANTE;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAforoApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAforoApp.class.getName());

	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 

	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
	 */
	private JsonObject tableConfig;

	/**
	 * Asociación a la clase principal del negocio.
	 */
	private AFOROCCANDES aforo;

	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
	/**
	 * Objeto JSON con la configuración de interfaz de la app.
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacción para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * Menú de la aplicación
	 */
	private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Construye la ventana principal de la aplicación. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazAforoApp( )
	{
		// Carga la configuración de la interfaz desde un archivo JSON
		guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);

		// Configura la apariencia del frame que contiene la interfaz gráfica
		configurarFrame ( );
		if (guiConfig != null) 	   
		{
			crearMenu( guiConfig.getAsJsonArray("menuBar") );
		}

		tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
		aforo = new AFOROCCANDES (tableConfig);

		String path = guiConfig.get("bannerPath").getAsString();
		panelDatos = new PanelDatos ( );

		setLayout (new BorderLayout());
		add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
		add( panelDatos, BorderLayout.CENTER );        
	}

	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
	/**
	 * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
	 * @param tipo - El tipo de configuración deseada
	 * @param archConfig - Archivo Json que contiene la configuración
	 * @return Un objeto JSON con la configuración del tipo especificado
	 * 			NULL si hay un error en el archivo.
	 */
	private JsonObject openConfig (String tipo, String archConfig)
	{
		JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
			//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
		return config;
	}

	/**
	 * Método para configurar el frame principal de la aplicación
	 */
	private void configurarFrame(  )
	{
		int alto = 0;
		int ancho = 0;
		String titulo = "";	

		if ( guiConfig == null )
		{
			log.info ( "Se aplica configuración por defecto" );			
			titulo = "Aforo APP Default";
			alto = 300;
			ancho = 500;
		}
		else
		{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
			titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
		}

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocation (50,50);
		setResizable( true );
		setBackground( Color.WHITE );

		setTitle( titulo );
		setSize ( ancho, alto);        
	}

	/**
	 * Método para crear el menú de la aplicación con base em el objeto JSON leído
	 * Genera una barra de menú y los menús con sus respectivas opciones
	 * @param jsonMenu - Arreglo Json con los menùs deseados
	 */
	private void crearMenu(  JsonArray jsonMenu )
	{    	
		// Creación de la barra de menús
		menuBar = new JMenuBar();       
		for (JsonElement men : jsonMenu)
		{
			// Creación de cada uno de los menús
			JsonObject jom = men.getAsJsonObject(); 

			String menuTitle = jom.get("menuTitle").getAsString();        	
			JsonArray opciones = jom.getAsJsonArray("options");

			JMenu menu = new JMenu( menuTitle);

			for (JsonElement op : opciones)
			{       	
				// Creación de cada una de las opciones del menú
				JsonObject jo = op.getAsJsonObject(); 
				String lb =   jo.get("label").getAsString();
				String event = jo.get("event").getAsString();

				JMenuItem mItem = new JMenuItem( lb );
				mItem.addActionListener( this );
				mItem.setActionCommand(event);

				menu.add(mItem);
			}       
			menuBar.add( menu );
		}        
		setJMenuBar ( menuBar );	
	}

	/* ****************************************************************
	 * 			CRUD DE BAÑO
	 *****************************************************************/
	public void adicionarBaño( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite el id del espacio y el número de sanitarios separado por comas",
					"Adicionar baño", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				String[] datos=id.split(",");
				String idEspacio=datos[0];
				String numSanitarios=datos[1];
				long idRealEspacio=Long.valueOf(idEspacio);;
				int numSanitarios2=Integer.parseInt(numSanitarios);
				VOBAÑO tb = aforo.adicionarBaño(idRealEspacio, numSanitarios2);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear un baño con id: " + idEspacio);
				}
				String resultado = "En adicionarBaño\n\n";
				resultado += "Baño adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darBaños( )
	{
		try 
		{
			List <VOBAÑO> lista = aforo.darVOBaños();

			String resultado = "En listar Baños";
			resultado +=  "\n" + listarBaños(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarBañoPorId( )
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del baño?", "Borrar baño por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = aforo.eliminarBañoPorId(idTipo);

				String resultado = "En eliminar Baño\n\n";
				resultado += tbEliminados + " Baños eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC10()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Fecha inicial, fecha final, id del establecimiento, orden"
					, "Visitantes en contacto ", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String fechaInicio=datos[0];
				String fechaFin= datos[1];
				String id= datos[2];
				String ordenar=datos[3];
				Long idE=Long.parseLong(id);
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());
				
				List <Object[]> visitantes= aforo.RFC10(idE, ts1, ts2, ordenar);
				String resultado = "Datos:";
				resultado +=  "\n" + listarObjectRFC10(visitantes);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";

			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC11()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Fecha inicial, fecha final, id del establecimiento, característica de ordenamiento"
					, "Visitantes en contacto ", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String fechaInicio=datos[0];
				String fechaFin= datos[1];
				String id= datos[2];
				Long idE=Long.parseLong(id);
				String consulta=datos[3];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());
				
				List <Object[]> visitantes= aforo.RFC11(idE, ts1, ts2, consulta);
				String resultado = "Datos:";
				resultado +=  "\n" + listarObjectRFC10(visitantes);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";

			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	private String listarObjectRFC10(List<Object[]> lista) 
	{
		String resp = "Los visitantes que entraton al establecimiento entre esas fechas fueron:\n";
		for (int i=0;i<lista.size();i++) 
		{ 
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visitante #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC11(List<Object[]> lista) 
	{
		String resp = "Los visitantes que no entraton al establecimiento entre esas fechas fueron:\n";
		for (int i=0;i<lista.size();i++) 
		{ 
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visitante #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}

	public void buscarBañoPorId( )
	{
		try 
		{
			String idBaño = JOptionPane.showInputDialog (this, "Id del baño?", "Buscar baño por Id", JOptionPane.QUESTION_MESSAGE);
			if (idBaño!= null)
			{
				Long idRealBaño=Long.getLong(idBaño);
				VOBAÑO baño = aforo.darBañoPorId(idRealBaño);
				String resultado = "En buscar Baño por Id\n\n";
				if (baño!= null)
				{
					resultado += "El tipo de bebida es: " + baño;
				}
				else
				{
					resultado += "Un baño con id: " + idRealBaño + " NO EXISTE\n";    				
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	/* ****************************************************************
	 * 			CRUD DE PARQUEADERO
	 *****************************************************************/
	public void adicionarParqueadero( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite el id del espacio y la capacidad separado por comas",
					"Adicionar parqueadero", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				String[] datos=id.split(",");
				String idEspacio=datos[0];
				String capacidad=datos[1];
				long idRealEspacio=Long.valueOf(idEspacio);
				int capacidadReal=Integer.parseInt(capacidad);
				VOPARQUEADERO tb = aforo.adicionarParqueadero(idRealEspacio, capacidadReal);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear un parqueadero con id espacio: " + idEspacio);
				}
				String resultado = "En adicionarParqueadero\n\n";
				resultado += "Parqueadero adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darParqueaderos( )
	{
		try 
		{
			List <VOPARQUEADERO> lista = aforo.darVOPARQUEADEROS();

			String resultado = "En listar Parqueaderos";
			resultado +=  "\n" + listarParqueaderos(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarParqueaderoPorId()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo del parqueadero?", "Borrar parqueadero por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = aforo.eliminarParqueaderoPorId(idTipo);

				String resultado = "En eliminar Parqueadero\n\n";
				resultado += tbEliminados + " Parqueaderos eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void buscarParqueaderoPorId( )
	{
		try 
		{
			String idParqueadero= JOptionPane.showInputDialog (this, "Id del parqueadero?", "Buscar parqueadero por Id", JOptionPane.QUESTION_MESSAGE);
			if (idParqueadero!= null)
			{
				Long idRealParqueadero=Long.getLong(idParqueadero);
				VOPARQUEADERO parqueadero = aforo.darParqueaderoPorId(idRealParqueadero);
				String resultado = "En buscar Parqueadero por Id\n\n";
				if (parqueadero!= null)
				{
					resultado += "El parqueadero es: " + parqueadero;
				}
				else
				{
					resultado += "Un parqueadero con id: " + idRealParqueadero+ " NO EXISTE\n";    				
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* ****************************************************************
	 * 			CRUD DE ESPACIO
	 *****************************************************************/
	public void adicionarEspacio( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite el horario apertura para empleados, el horario apertura para clientes, horario cierre, aforo total, aforo actual y el estado separado por comas",
					"Adicionar Espacio (Insertar Fechas en formato yyyy-MM-dd HH:mm:ss.SSS)", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				String[] datos=id.split(",");
				String horarioAperturaEmpleados=datos[0];
				String horarioAperturaClientes=datos[1];
				String horarioCierreClientes = datos[2];
				String aforoActual = datos[3];
				String aforoTotal = datos[4];
				String estado = datos[5];
				int aforoTotal1=Integer.parseInt(aforoTotal);
				int aforoActual1=Integer.parseInt(aforoActual);

				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hCC = formatter.parse(horarioCierreClientes);
				Date hAC= formatter.parse(horarioAperturaClientes);
				Date hAE = formatter.parse(horarioAperturaEmpleados);

				Timestamp  ts1 = new Timestamp(hAE.getTime());
				Timestamp  ts2 = new Timestamp(hAC.getTime());
				Timestamp  ts3 = new Timestamp(hCC.getTime());

				VOESPACIO tb = aforo.adicionarEspacio(ts1,ts2,ts3,aforoActual1,aforoTotal1,estado);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear Espacio con aforo total: " +aforoTotal);
				}
				String resultado = "En adicionarEspacio\n\n";
				resultado += "Parqueadero adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darEspacios( )
	{
		try 
		{
			List <VOESPACIO> lista = aforo.darVOBEspacios();

			String resultado = "En listar Espacios";
			resultado +=  "\n" + listarEspacios(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarEspacioPorId()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo del Espacio?", "Borrar parqueadero por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = aforo.eliminarEspacioPorId(idTipo);

				String resultado = "En eliminar Espacio\n\n";
				resultado += tbEliminados + " Espacio eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC3AdminEstablecimiento()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del espacio del que quiere saber el aforo", "Aforo del establecimiento- AdminEstablecimiento", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idEspacio = Long.valueOf(idTipoStr);
				List<Object[]> informacion= aforo.RFC3AdminEstablecimiento(idEspacio);
				String resultado = "Indices de los aforos - AdminEstablecimiento";
				resultado +=  "\n" + listarObjectRFC3AdminEstablecimiento(informacion);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC3AdminCentro()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del espacio del que quiere saber el aforo", "Aforo del establecimiento- AdminEstablecimiento", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idEspacio = Long.valueOf(idTipoStr);
				List<Object[]> informacion= aforo.RFC3AdminCentro(idEspacio);
				String resultado = "Indices de los aforos - AdminCentro";
				resultado +=  "\n" + listarObjectRFC3AdminEstablecimiento(informacion);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC3AdminCentroPorTipo()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del espacio del que quiere saber el aforo", "Aforo del establecimiento- AdminEstablecimiento", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				List<Object[]> informacion= aforo.RFC3AdminCentroPorTipo(idTipoStr);
				String resultado = "Indices de los aforos - AdminCentro";
				resultado +=  "\n" + listarObjectRFC3AdminEstablecimiento(informacion);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC4()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Digite cualquier letra para ver los espacios con aforo disponible", "Aforos disponibles", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				List<Object[]> informacion= aforo.RFC4();
				String resultado = "Espacios con aforo disponible";
				resultado +=  "\n" + listarObjectRFC4(informacion);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RF9()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Insertar id del espacio y el estado a cambiar separados por comas", "Registrar cambio de estado de un Espacio", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String id=datos[0];
				String estado=datos[1];
				long idEspacio = Long.valueOf(id);
				aforo.RF9(estado, idEspacio);
				String resultado = "Registro exitoso!";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RF11()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Insertar id del espacio y 'Deshabilitar'  separados por comas para deshabilitar el espacio", "Deshabilitar un espacio", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String id=datos[0];
				String estado=datos[1];
				long idEspacio = Long.valueOf(id);
				aforo.RF11(estado, idEspacio);
				String resultado = "Registro exitoso!";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RF12()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Insertar id del espacio y el estado a cambiar separados por comas", "Registrar cambio de estado de un Espacio", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String id=datos[0];
				String estado=datos[1];
				long idEspacio = Long.valueOf(id);
				aforo.RF11(estado, idEspacio);
				String resultado = "Registro exitoso!";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* ****************************************************************
	 * 			CRUD DE VISITA
	 *****************************************************************/
	public void adicionarVisita( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite la fecha y hora operacion, el tipo de operacion, horario fin operacion,  id lector,id carnet e id espacio actual separado por comas",
					"Adicionar Visitas (Insertar Fechas/Horario en formato yyyy-MM-dd HH:mm:ss.SSS)", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				String[] datos=id.split(",");
				String fechaYHoraOp=datos[0];
				String tipoOp=datos[1];
				String horaFin = datos[2];
				String idCarnet = datos[4];
				String idLector = datos[3];
				String idEspacio = datos[5];
				long idLector1=Long.valueOf(idLector);
				long idEspacio1=Long.valueOf(idEspacio);
				long idCarnet1 = Long.valueOf(idCarnet);

				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaYHoraOp);
				Date hFin= formatter.parse(horaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());

				VOVISITA tb = aforo.adicionarVisita(ts1,tipoOp,ts2,idLector1,idCarnet1,idEspacio1);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear Visita con idLector: " +idLector+",idEspacio: "+idEspacio+"e idCarnet: "+idCarnet);
				}
				String resultado = "En adicionarVisita\n\n";
				resultado += "Visita adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darVisitas( )
	{
		try 
		{
			List <VOVISITA> lista = aforo.darVOVisita();

			String resultado = "En listar visitas";
			resultado +=  "\n" + listarVisitas(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarVisitaPorId()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo del Lector, Carnet y Espacio?", "Borrar visita por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String idEspacio1=datos[2];
				String idLector2=datos[0];
				String idCarnet3= datos[1];

				long idEspacio = Long.valueOf(idEspacio1);
				long idLector = Long.valueOf(idLector2);
				long idCarnet = Long.valueOf(idCarnet3);

				long tbEliminados = aforo.eliminarVisita(idCarnet, idLector, idEspacio);

				String resultado = "En eliminar Visita\n\n";
				resultado += tbEliminados + " Visita eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC1AdminEstablecimiento()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del espacio, fecha inicio y fecha fin (fechas formato yyyy-MM-dd HH:mm:ss.SSS)", "Visitantes por espacio - Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String idEspacio1=datos[0];
				String fechaInicio=datos[1];
				String fechaFin= datos[2];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());

				long idEspacio = Long.valueOf(idEspacio1);


				List <Object[]> visitantesAtendidos= aforo.RFC1AdminEstablecimiento(idEspacio, ts1, ts2);
				String resultado = "Visitas de establecimiento a una fecha dada - Admin";
				resultado +=  "\n" + listarObjectRFC1Admin(visitantesAtendidos);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC1AdminCentro()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Fecha inicio y fecha fin (fechas formato yyyy-MM-dd HH:mm:ss.SSS)", "Visitantes por espacio - Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String fechaInicio=datos[0];
				String fechaFin= datos[1];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());



				List <Object[]> visitantesAtendidos= aforo.RFC1AdminCentro(ts1, ts2);
				String resultado = "Visitas de establecimiento a una fecha dada - AdminCentro";
				resultado +=  "\n" + listarObjectRFC1AdminC(visitantesAtendidos);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC2()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Fecha inicio y fecha fin (fechas formato yyyy-MM-dd HH:mm:ss.SSS)", "Visitantes por espacio - Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String fechaInicio=datos[0];
				String fechaFin= datos[1];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());



				List <Object[]> establecimientos= aforo.RFC2(ts1, ts2);
				String resultado = "Top 20 establecimientos más visitados";
				resultado +=  "\n" + listarObjectRFC2(establecimientos);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC7()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Fecha inicial, fecha final (fechas formato yyyy-MM-dd HH:mm:ss.SSS) y tipo establecimiento"
					, "Operacion CC Andes", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String fechaInicio=datos[0];
				String fechaFin= datos[1];
				String tipo= datos[2];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());
				
				List <Object[]> visitantes= aforo.RFC7(ts1,ts2,tipo);
				String resultado = "Datos:";
				resultado +=  "\n" + listarObjectRFC7(visitantes);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC9()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Fecha en la que quiere saber los contactos entre visitantes (de esta fecha hasta 10 dias antes)"
					, "Visitantes en contacto ", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(idTipoStr);

				Timestamp  ts1 = new Timestamp(hOp.getTime());

				//
				List <Object[]> visitantes= aforo.RFC9(ts1);
				String resultado = "Visitantes en contacto:";
				resultado +=  "\n" + listarObjectRFC9(visitantes);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//   			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* ****************************************************************
	 * 			CRUD DE VISITANTE
	 *****************************************************************/
	public void adicionarVisitante( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite la cedula, nombre,telefono, nombre contacto, telefono contacto, codigoQR, correo, horarioDisponibilidad, tipo de visitante, id espacio y estado separado por comas",
					"Adicionar Visitante (Insertar Fechas/Horario en formato yyyy-MM-dd HH:mm:ss.SSS) y tipoVisitante acorde a los tipos de la documentacion", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				String[] datos=id.split(",");
				String cedula1=datos[0];
				String nombre=datos[1];
				String telefono1 = datos[2];
				String nombreContacto = datos[3];
				String telefonoContacto1 = datos[4];
				String codigoQr = datos[5];
				String correo=datos[6];
				String horarioDisponibilidad=datos[7];
				String tipoVisitante = datos[8];
				String idEspacio1 = datos[9];
				String estado = datos[10];

				long cedula = Long.valueOf(cedula1);
				long idEspacio= Long.valueOf(idEspacio1);

				float telefono = Float.valueOf(telefono1);
				float telefonoContacto = Float.valueOf(telefonoContacto1);

				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(horarioDisponibilidad);

				Timestamp  ts1 = new Timestamp(hOp.getTime());

				VOVISITANTE tb = aforo.adicionarVisitante(cedula,nombre,telefono,nombreContacto,telefonoContacto,codigoQr,correo,ts1,tipoVisitante,idEspacio, estado);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear Visitante con cedula:"+cedula);
				}
				String resultado = "En adicionarVisitante\n\n";
				resultado += "Visitante adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darVisitantes( )
	{
		try 
		{
			List <VOVISITANTE> lista = aforo.darVOBeVisitantes();

			String resultado = "En listar Visitante";
			resultado +=  "\n" + listarVisitantes(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarVisitantePorCedula()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Cedula del visitante?", "Borrar visitante por Cedula", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				float id = Float.valueOf(idTipoStr);

				long tbEliminados = aforo.eliminarVisitantePorCedula(id);

				String resultado = "En eliminar Visitante\n\n";
				resultado += tbEliminados + " Visitante eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarVisitantesPorNombre()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Nombre del visitante a borrar?", "Borrar visitante por nombre", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{      		
				long tbEliminados = aforo.eliminarVisitantePorNombre(idTipoStr);
				String resultado = "En eliminar Visitante\n\n";
				resultado += tbEliminados + " Visitante eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC5PorTipoVisitante()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Cedula del visitante, fecha inicio y fecha fin (fechas formato yyyy-MM-dd HH:mm:ss.SSS)", "Comportamiento de un visitante- Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String tipo=datos[0];
				String fechaInicio=datos[1];
				String fechaFin= datos[2];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());          			

				List <Object[]> visitantes= aforo.RFC5PorTipoVisitante(tipo, ts1, ts2);
				String resultado = "Visitas de un tipo de visitante en una fecha dada - Admin";
				resultado +=  "\n" + listarObjectRFC5PorTipoVisitante(visitantes);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC6()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Cedula del visitante, fecha inicio y fecha fin (fechas formato yyyy-MM-dd HH:mm:ss.SSS)", "Comportamiento de un visitante- Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String id=datos[0];
				String fechaInicio=datos[1];
				String fechaFin= datos[2];
				long cedula=Long.valueOf(id);
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());          			

				List <Object[]> visitasVisitante= aforo.RFC6(cedula, ts1, ts2);
				String resultado = "Visitas a establecimiento de un visitante en una fecha dada - Admin";
				resultado +=  "\n" + listarObjectRFC6(visitasVisitante);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RF8()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Insertar cedula del visitante y el estado a cambiar separados por comas", "Registrar cambio de estado de un Espacio", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String id=datos[0];
				String estado=datos[1];
				long idEspacio = Long.valueOf(id);
				aforo.RF11(estado, idEspacio);
				String resultado = "Registro exitoso!";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* *
	/* ****************************************************************
	 * 			CRUD DE CARNET
	 *****************************************************************/
	public void adicionarCarnet( )
	{
		try 
		{
			String cedula = JOptionPane.showInputDialog (this, "Digite la cedula.",
					"Adicionar Carnet", JOptionPane.QUESTION_MESSAGE);
			if (cedula!= null)
			{
				float cedulaReal=Float.parseFloat(cedula);
				VOCARNET carnet=aforo.adicionarCarnet(cedulaReal);
				if (carnet == null)
				{
					throw new Exception ("No se pudo crear un carnet con la cedula: " + cedulaReal);
				}
				String resultado = "En adicionarCarnet\n\n";
				resultado += "Carnet adicionado exitosamente: " + carnet;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darCarnets( )
	{
		try 
		{
			List <VOCARNET> lista = aforo.darVOCarnets();

			String resultado = "En listar CARNETS";
			resultado +=  "\n" + listarCarnets(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarCarnetPorId( )
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del carnet?", "Borrar carnet por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = aforo.eliminarCarnetPorId(idTipo);

				String resultado = "En eliminar Carnet\n\n";
				resultado += tbEliminados + " Carnet eliminado\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void buscarCarnetPorId( )
	{
		try 
		{
			String idCarnet = JOptionPane.showInputDialog (this, "Id del Carnet?", "Buscar carnet por Id", JOptionPane.QUESTION_MESSAGE);
			if (idCarnet!= null)
			{
				Long idRealCarnet=Long.getLong(idCarnet);
				VOCARNET carnet= aforo.darCarnetPorId(idRealCarnet);
				String resultado = "En buscar Baño por Id\n\n";
				if (carnet!= null)
				{
					resultado += "El tipo de bebida es: " + carnet;
				}
				else
				{
					resultado += "Un carnet con id: " + idRealCarnet + " NO EXISTE\n";    				
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void cambiarCedulaCarnet( )
	{
		try 
		{
			String carnet = JOptionPane.showInputDialog (this, "Digite el IdCarnet y la cedula", "Cambiar cedula de carnet", JOptionPane.QUESTION_MESSAGE);
			if (carnet!= null)
			{
				String[] datos=carnet.split(",");
				String idCarnet=datos[0];
				String cedula=datos[1];
				Long idCarnetReal=Long.getLong(idCarnet);
				float cedulaReal=Float.parseFloat(cedula);
				CARNET carnetBuscado=aforo.cambiarCedulaCarnet(idCarnetReal, cedulaReal);
				String resultado = "En cambiar cedula carnet\n\n";
				if (carnetBuscado!= null)
				{
					resultado += "El carnet cambiado es: " + carnetBuscado;
				}
				else
				{
					resultado += "Un carnet con id: " + idCarnet + " NO EXISTE\n";    				
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* ****************************************************************
	 * 			CRUD DE LECTOR
	 *****************************************************************/
	public void adicionarLector( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite el id del espacio.",
					"Adicionar Lector", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				long idRealEspacio=Long.valueOf(id);
				VOLECTOR tb = aforo.adicionarLector(idRealEspacio);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear un lector con idEspacio: " + id);
				}
				String resultado = "En adicionarLector\n\n";
				resultado += "Lector adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darLectores( )
	{
		try 
		{
			List <VOLECTOR> lista = aforo.darVOLector();

			String resultado = "En listar Lectores";
			resultado +=  "\n" + listarLectores(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarLectorPorId( )
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del lector?", "Borrar lector por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = aforo.eliminarLector(idTipo);

				String resultado = "En eliminar Lector\n\n";
				resultado += tbEliminados + " Lectores eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void buscarLectorPorId( )
	{
		try 
		{
			String idLector= JOptionPane.showInputDialog (this, "Id del lector?", "Buscar lector por Id", JOptionPane.QUESTION_MESSAGE);
			if (idLector!= null)
			{
				long idRealLector=Long.getLong(idLector);
				VOLECTOR lector = aforo.darLectorPorId(idRealLector);
				String resultado = "En buscar Baño por Id\n\n";
				if (lector!= null)
				{
					resultado += "El tipo de bebida es: " + lector;
				}
				else
				{
					resultado += "Un baño con id: " + idLector + " NO EXISTE\n";    				
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* ****************************************************************
	 * 			CRUD DE LOCAL COMERCIAL
	 *****************************************************************/
	public void adicionarLocalComercial( )
	{
		try 
		{
			String id = JOptionPane.showInputDialog (this, "Digite el id del espacio, nombre, nombre de la empresa, area y el tipo de establecimiento.",
					"Adicionar Local Comercial", JOptionPane.QUESTION_MESSAGE);
			if (id!= null)
			{
				String[] datos=id.split(",");
				String idEspacio=datos[0];
				String nombre=datos[1];
				String nombreEmpresa=datos[2];
				String area=datos[3];
				String tipoEstablecimiento=datos[4];
				long idRealEspacio=Long.valueOf(idEspacio);
				float areaReal=Float.parseFloat(area);
				VOLOCAL_COMERCIAL tb = aforo.adicionarLocalComercial(idRealEspacio, nombreEmpresa, nombreEmpresa, areaReal, tipoEstablecimiento);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear un local comercial con nombre: " + nombre);
				}
				String resultado = "En adicionar Local Comercial\n\n";
				resultado += "Local comercial adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darLocalesComerciales( )
	{
		try 
		{
			List <VOLOCAL_COMERCIAL> lista = aforo.darVOLOCAL_COMERCIAL();
			String resultado = "En listar locales comerciales";
			resultado +=  "\n" + listarLocalesComerciales(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarLocalComercialPorId( )
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del local comercial?", "Borrar local comercial por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = aforo.eliminarLocalComercialPorId(idTipo);

				String resultado = "En eliminar local comercial por Id\n\n";
				resultado += tbEliminados + " Locales comerciales eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void eliminarLocalComercialPorNombre( )
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Nombre del local comercial?", "Borrar local comercial por nombre", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long tbEliminados = aforo.eliminarLocalComercialPorNombre(idTipoStr);
				String resultado = "En eliminar local comercial por nombre\n\n";
				resultado += tbEliminados + " Locales comerciales eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void buscarLocalComercialPorId( )
	{
		try 
		{
			String idLocal= JOptionPane.showInputDialog (this, "Id del local comercial?", "Buscar local comercial por Id", JOptionPane.QUESTION_MESSAGE);
			if (idLocal!= null)
			{
				Long idRealLocal=Long.getLong(idLocal);
				VOLOCAL_COMERCIAL local= aforo.darLocalPorId(idRealLocal);
				String resultado = "En buscar Baño por Id\n\n";
				if (local!= null)
				{
					resultado += "El tipo de bebida es: " + local;
				}
				else
				{
					resultado += "Un baño con id: " + idLocal + " NO EXISTE\n";    				
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void darLocalComercialesPorNombre( )
	{
		try 
		{
			String nombre= JOptionPane.showInputDialog (this, "Nombre del local comercial?", "Buscar local comercial por nombre", JOptionPane.QUESTION_MESSAGE);
			if (nombre!= null)
			{
				List <VOLOCAL_COMERCIAL> lista = aforo.darLocalPorNombre(nombre);
				String resultado = "En listar locales comerciales";
				if(lista.size()!=0)
				{
					resultado +=  "\n" + listarLocalesComerciales(lista);
					panelDatos.actualizarInterfaz(resultado);
					resultado += "\n Operación terminada";
				}else{
					resultado += "Un local con nombre: " + nombre + " NO EXISTE\n"; 
				}
			}
			else{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC5PorTipoLocal()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Tipo de local comercial, fecha inicio y fecha fin (fechas formato yyyy-MM-dd HH:mm:ss.SSS)", "Comportamiento tipo local - Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				String[] datos=idTipoStr.split(",");
				String tipo=datos[0];
				String fechaInicio=datos[1];
				String fechaFin= datos[2];
				//Convertir fechas
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
				DateFormat formatter = new SimpleDateFormat(FORMAT);

				Date hOp = formatter.parse(fechaInicio);
				Date hFin= formatter.parse(fechaFin);

				Timestamp  ts1 = new Timestamp(hOp.getTime());
				Timestamp  ts2 = new Timestamp(hFin.getTime());          			

				List <Object[]> locales= aforo.RFC5PorTipoLocal(tipo, ts1, ts2);
				String resultado = "Comportamiento locales a una fecha dada - Admin";
				resultado +=  "\n" + listarObjectRFC5PorTipoLocal(locales);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void RFC8()
	{
		try 
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Nombre del local comercial", "Clientes frecuentes local- Admin", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				List <Object[]> locales= aforo.RFC8(idTipoStr);
				String resultado = "Clientes frecuentes del local:";
				resultado +=  "\n" + listarObjectRFC8(locales);
				panelDatos.actualizarInterfaz(resultado);
				resultado += "\n Operación terminada";
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//      			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}

	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}

	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
			// Ejecución de la demo y recolección de los resultados
			long eliminados [] = aforo.limpiarAforo();

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Banios eliminados\n";
			resultado += eliminados [1] + " Centro_comercial eliminados\n";
			resultado += eliminados [2] + " Espacio eliminados\n";
			resultado += eliminados [3] + " Lector eliminadas\n";
			resultado += eliminados [4] + " Parqueadero eliminados\n";
			resultado += eliminados [5] + " Visita eliminados\n";
			resultado += eliminados [6] + " Visitantes eliminados\n";
			resultado += eliminados [6] + " Local_comercial eliminados\n";
			resultado += "\nLimpieza terminada";

			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}

	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}

	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}

	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}

	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}

	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}

	/**
	 * Muestra la información acerca del desarrollo de esta apicación
	 */
	public void acercaDe ()
	{
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: AforoCC Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Juan David Cardona y Nicolas Quintero\n";
		resultado += " * 2020 RealGMusic\n";
		resultado += " * \n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
	}


	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
	/**
	 * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
	 * @param lista - La lista con los tipos de bebida
	 * @return La cadena con una líea para cada tipo de bebida recibido
	 */
	private String listarParqueaderos(List<VOPARQUEADERO> lista) 
	{
		String resp = "Los parqueaderos existentes son:\n";
		int i = 1;
		for (VOPARQUEADERO tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarLocalesComerciales(List<VOLOCAL_COMERCIAL> lista) 
	{
		String resp = "Los locales existentes son:\n";
		int i = 1;
		for (VOLOCAL_COMERCIAL tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarLectores(List<VOLECTOR> lista) 
	{
		String resp = "Los Lectores existentes son:\n";
		int i = 1;
		for (VOLECTOR tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarBaños(List<VOBAÑO> lista) 
	{
		String resp = "Los baños existentes son:\n";
		int i = 1;
		for (VOBAÑO tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarCarnets(List<VOCARNET> lista) 
	{
		String resp = "Los Carnets existentes son:\n";
		int i = 1;
		for (VOCARNET tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarEspacios(List<VOESPACIO> lista) 
	{
		String resp = "Los espacios existentes son:\n";
		int i = 1;
		for (VOESPACIO tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}

	private String listarVisitas(List<VOVISITA> lista) 
	{
		String resp = "Las visitas existentes son:\n";
		int i = 1;
		for (VOVISITA tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarVisitantes(List<VOVISITANTE> lista) 
	{
		String resp = "Los visitantes existentes son:\n";
		int i = 1;
		for (VOVISITANTE tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarObjectRFC1Admin(List<Object[]> lista) 
	{
		String resp = "Los visitantes atendidos por ese establecimiento (a esas horas) son:\n";
		for (int i=0;i<lista.size();i++) 
		{
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visitante #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC1AdminC(List<Object[]> lista) 
	{
		String resp = "Los visitantes atendidos (a esas horas) son:\n";
		for (int i=0;i<lista.size();i++) 
		{
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visitante #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC2(List<Object[]> lista) 
	{
		String resp = "Los 20 establecimientos más populares son:\n";
		for (int i=0;i<lista.size();i++) 
		{
			int indice=i+1;
			resp+="------------------\n";
			resp+="Establecimiento #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC3AdminEstablecimiento(List<Object[]> lista) 
	{
		String resp = "Los indices son:\n";
		for (int i=0;i<lista.size();i++) 
		{
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC4(List<Object[]> lista) 
	{
		String resp = "Los espacios con aforo disponible son:\n";
		for (int i=0;i<lista.size();i++) 
		{
			int indice=i+1;
			resp+="------------------\n";
			resp+="Establecimiento #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC5PorTipoVisitante(List<Object[]> lista) 
	{
		String resp = "Historial de visitantes:\n";
		int maximoHoras=0, maximoSegundos=0, maximoMinutos=0;
		int minimoHoras=99, minimoSegundos=99, minimoMinutos=99;
		int sumaHoras=0, sumaMinutos=0, sumaSegundos=0;
		for (int i=0;i<lista.size();i++) 
		{
			int horasActuales=0;
			int minutosActuales=0;
			int segundosActuales=0;
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visita #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{	
				if(j==6)
				{
					horasActuales=(Integer)lista.get(i)[j];
					resp+="Horas de la visita: ";
					sumaHoras+=horasActuales;

				}
				else if(j==7)
				{
					minutosActuales=(Integer)lista.get(i)[j];
					resp+="Minutos de la visita: ";
					sumaMinutos+=minutosActuales;
				}
				else if(j==8)
				{
					segundosActuales=(Integer)lista.get(i)[j];
					resp+="Segundos de la visita: ";
					sumaSegundos+=segundosActuales;
				}
				resp +=lista.get(i)[j]+"\n";
			}
			//Maximo tiempo visita
			if(maximoHoras<horasActuales)
			{
				maximoHoras=horasActuales;
				maximoMinutos=minutosActuales;
				maximoSegundos=segundosActuales;
			}else if(maximoHoras==horasActuales && maximoMinutos<minutosActuales)
			{
				maximoMinutos=minutosActuales;
				maximoSegundos=segundosActuales;
			}else if(maximoHoras==horasActuales && maximoMinutos==minutosActuales && maximoSegundos<segundosActuales)
			{
				maximoSegundos=segundosActuales;
			}
			//Minimo tiempo visita
			if(minimoHoras>horasActuales)
			{
				minimoHoras=horasActuales;
				minimoMinutos=minutosActuales;
				minimoSegundos=segundosActuales;
			}else if(minimoHoras==horasActuales && minimoMinutos>minutosActuales)
			{
				minimoMinutos=minutosActuales;
				minimoSegundos=segundosActuales;
			}else if(minimoHoras==horasActuales && minimoMinutos==minutosActuales && minimoSegundos>segundosActuales)
			{
				minimoSegundos=segundosActuales;
			}
		}
		resp+="---------------------------------------------------------------------------------------\n";
		resp+="En total, las visitas sumaron "+sumaHoras+" horas , "+sumaMinutos+" minutos y "+
				sumaSegundos+" segundos.\n";
		resp+="Por visita se obtuvo un promedio de "+((float)sumaHoras/lista.size())+" horas , "+((float)sumaMinutos/lista.size())+" minutos y "+
				((float)sumaSegundos/lista.size())+" segundos.\n";
		resp+="La visita más larga fue de "+maximoHoras+" horas , "+maximoMinutos+" minutos y "+
				maximoSegundos+" segundos.\n";
		resp+="La visita más corta fue de "+minimoHoras+" horas , "+minimoMinutos+" minutos y "+
				minimoSegundos+" segundos.\n";
		return resp;

	}
	private String listarObjectRFC5PorTipoLocal(List<Object[]> lista) 
	{
		String resp = "Historial de locales:\n";
		String maximoLocal="";
		String minimoLocal="";
		int sumaVisitas=0;
		int maximoVisitas=0;
		int minimoVisitas=99;
		for (int i=0;i<lista.size();i++) 
		{
			int numVisitasActuales=0;
			String establecimientoActual="";
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visita #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{	
				if(j==1)
				{
					establecimientoActual=(String)lista.get(i)[j];
					resp+="Nombre de establecimiento: ";
				}
				else if(j==2)
				{
					numVisitasActuales=(Integer)lista.get(i)[j];
					resp+="Numero de visitas: ";
					sumaVisitas+=numVisitasActuales;

				}
				resp +=lista.get(i)[j]+"\n";
			}
			//Maximas visitas
			if(maximoVisitas<numVisitasActuales)
			{
				maximoVisitas=numVisitasActuales;
				maximoLocal=establecimientoActual;
				
			}
			//Minimo tiempo visita
			if(minimoVisitas>numVisitasActuales)
			{
				minimoVisitas=numVisitasActuales;
				minimoLocal=establecimientoActual;

			}
		}
		resp+="---------------------------------------------------------------------------------------\n";
		resp+="En total, las visitas fueron "+sumaVisitas+" en ese intervalo de tiempo.\n";
		resp+="El local con mas visitas fue "+maximoLocal+" con "+maximoVisitas+" visitas. \n";
		resp+="El local con menos visitas fue "+minimoLocal+" con "+minimoVisitas+" visitas. \n";
		return resp;

	}
	private String listarObjectRFC6(List<Object[]> lista) 
	{
		String resp = "Historial de visitas del visitante:\n";
		for (int i=0;i<lista.size();i++) 
		{
			int horasActuales=0, minutosActuales=0, segundosActuales=0;
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visita #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{	
				if(j==7)
				{
					horasActuales=(Integer)lista.get(i)[j];
					resp+="Horas de la visita: ";

				}
				else if(j==8)
				{
					minutosActuales=(Integer)lista.get(i)[j];
					resp+="Minutos de la visita: ";
				}
				else if(j==9)
				{
					segundosActuales=(Integer)lista.get(i)[j];
					resp+="Segundos de la visita: ";
				}
				resp +=lista.get(i)[j]+"\n";
			}
			resp+="Esta visita duro "+horasActuales+" horas , "+minutosActuales+" minutos y "+
					segundosActuales+" segundos.\n";
			
		}
		return resp;

	}
	private String listarObjectRFC7(List<Object[]> lista) 
	{
		String resp = "Analizando la operación de AFORO CC- ANDES:\n";
		int aforoT=(Integer)lista.get(0)[1];
		for (int i=0;i<lista.size();i++) 
		{
			if(aforoT==(Integer)lista.get(i)[1])
			{
				resp+="------------------\n";
				resp+="Fecha de Mayor influencia\n";
				resp+="------------------\n";
			}
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC8(List<Object[]> lista) 
	{
		String resp = "Los usuarios mas frecuentes son:\n";
		for (int i=0;i<lista.size();i++) 
		{
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visitante #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	private String listarObjectRFC9(List<Object[]> lista) 
	{
		String xd="0";
		long idEspacioInicial=Long.parseLong(xd);
		String resp = "Los visitantes en contacto en el mismo lugar a la misma hora fueron:\n";
		for (int i=0;i<lista.size();i++) 
		{ 
			long idEspacioActual=(long)lista.get(i)[6];
			if(idEspacioInicial!=idEspacioActual)
			{
				idEspacioInicial=idEspacioActual;
				resp+="|-----------------------------------------------------------------------|\n";
				resp+="|-----------------------------------------------------------------------|\n";
				resp+="|ESPACIO CON ID: "+idEspacioInicial+"                                                        |\n";
				resp+="|-----------------------------------------------------------------------|\n";
				resp+="|-----------------------------------------------------------------------|\n";
			}
			int indice=i+1;
			resp+="------------------\n";
			resp+="Visitante #"+indice+"\n";
			resp+="------------------\n";
			for(int j=0;j<lista.get(i).length;j++)
			{
				resp+=j==6?"Id del espacio visitado: ":"";
				resp +=lista.get(i)[j]+"\n";
			}
		}
		return resp;
	}
	/**
	 * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
	 * @param e - La excepción recibida
	 * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
			//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
	/**
	 * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
	 * Invoca al método correspondiente según el evento recibido
	 * @param pEvento - El evento del usuario
	 */
	@Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
		try 
		{
			Method req = InterfazAforoApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
	/**
	 * Este método ejecuta la aplicación, creando una nueva interfaz
	 * @param args Arreglo de argumentos que se recibe por línea de comandos
	 */
	public static void main( String[] args )
	{
		try
		{

			// Unifica la interfaz para Mac y para Windows.
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
			InterfazAforoApp interfaz = new InterfazAforoApp( );
			interfaz.setVisible( true );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
