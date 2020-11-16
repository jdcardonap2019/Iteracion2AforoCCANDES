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

package uniandes.isis2304.parranderos.interfazDemo;

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
import uniandes.isis2304.parranderos.interfazApp.PanelDatos;
import uniandes.isis2304.parranderos.negocio.AFOROCCANDES;
import uniandes.isis2304.parranderos.negocio.VOBAÑO;
import uniandes.isis2304.parranderos.negocio.VOCARNET;
import uniandes.isis2304.parranderos.negocio.VOCENTRO_COMERCIAL;
import uniandes.isis2304.parranderos.negocio.VOESPACIO;
import uniandes.isis2304.parranderos.negocio.VOLECTOR;
import uniandes.isis2304.parranderos.negocio.VOLOCAL_COMERCIAL;
import uniandes.isis2304.parranderos.negocio.VOPARQUEADERO;
import uniandes.isis2304.parranderos.negocio.VOVISITA;
import uniandes.isis2304.parranderos.negocio.VOVISITANTE;

/**
 * Clase principal de la interfaz
 * 
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazParranderosDemo extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazParranderosDemo.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigDemo.json"; 
	
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
    public InterfazParranderosDemo( )
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
        aforo = new AFOROCCANDES(tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos para la configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
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
			titulo = "Parranderos APP Default";
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
	 * 			Demos de ESPACIOS
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de ESPACIOS
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    public void demoEspacio( )
    {
    	try 
    	{
    		//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			List <VOESPACIO> lista = aforo.darVOBEspacios();
			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Espacio\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorEspacio)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "Adicionado el tipo de bebida con id: " + espacio.getID_ESPACIO()+ "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarEspacios(lista);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += espaciosEliminados + " Tipos de bebida eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
	/* ****************************************************************
	 * 			Demos de TipoBebida
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de Baños
     * Incluye también los espacios pues los espacios es llave foránea en los baños
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    public void demoBaño( )
    {
    	try 
    	{
    		//BRO, supuestamente en los documentos dice que los datos no pueden estar en el código
    		//pero no sé cómo más hacerlos,pregúntale a tu pana que cómo hizo para cargar los datos de prueba,
    		//de igual manera no puedo probar nada porque no tengo internet jaja :( me tocó así
    		//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			//BAÑO
			VOBAÑO baño= aforo.adicionarBaño(espacio.getID_ESPACIO(),3);
			List <VOBAÑO> lista = aforo.darVOBaños();
			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
			long bañosEliminados = aforo.eliminarBañoPorId(baño.getID_BAÑO());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Baño\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
			if (errorEspacio)
			{
				resultado += "*** Exception creando el espacio!!\n";
				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el id del Espacio\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "Adicionado el espacio con id: " + espacio.getID_ESPACIO()+ "\n";
			resultado += "Adicionada el baño con id: " + baño.getID_BAÑO()+ "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarEspacios (listaEspacio);
			resultado += "\n" + listarBaños(lista);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += bañosEliminados + " Parqueaderos eliminados\n";
			resultado += espaciosEliminados + " Espacios eliminados\n";
			resultado += "\n Demo terminada";
   
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			Demos de Parqueadero
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de Parqueaderos.
     * Incluye también los espacios pues los espacios es llave foránea en los parqueaderos
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
	public void demoParqueadero( )
    {
    	try 
    	{
    		//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			//PARQUEADERO
			VOPARQUEADERO parqueadero= aforo.adicionarParqueadero(espacio.getID_ESPACIO(), 5);
			
			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
			List <VOPARQUEADERO> listaParqueaderos= aforo.darVOPARQUEADEROS();
			long parqueaderosEliminados = aforo.eliminarParqueaderoPorId(parqueadero.getID_PARQUEADERO());
			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Parqueadero\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
			if (errorEspacio)
			{
				resultado += "*** Exception creando el espacio!!\n";
				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el id del Espacio\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "Adicionado el espacio con id: " + espacio.getID_ESPACIO()+ "\n";
			resultado += "Adicionada el parqueadero con id: " + parqueadero.getID_PARQUEADERO() + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarEspacios (listaEspacio);
			resultado += "\n" + listarParqueaderos(listaParqueaderos);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += parqueaderosEliminados + " Parqueaderos eliminados\n";
			resultado += espaciosEliminados + " Espacios eliminados\n";
			resultado += "\n Demo terminada";
   
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
     * Demostración de creación y borrado de bebidas no servidas.
     * Incluye también los tipos de bebida pues el tipo de bebida es llave foránea en las bebidas
     * Caso 1: Ninguna bebida es servida en ningún bar: La tabla de bebidas queda vacía antes de la fase de limpieza
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
	/*
    public void demoElimNoServidas1 ( )
    {
    	try 
    	{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			String nombreTipoBebida = "Vino tinto";
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida (nombreTipoBebida);
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre (nombreTipoBebida);
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida("120", tipoBebida.getId (), 10);
			VOBebida bebida2 = parranderos.adicionarBebida("Gato Negro", tipoBebida.getId (), 11);
			VOBebida bebida3 = parranderos.adicionarBebida("Don Pedro", tipoBebida.getId (), 12);
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas1 = parranderos.darVOBebidas();
			List <VOSirven> listaSirven = parranderos.darVOSirven ();
			long noServidasEliminadas = parranderos.eliminarBebidasNoServidas();
			List <VOBebida> listaBebidas2 = parranderos.darVOBebidas();
			
			long bebEliminadas1 = parranderos.eliminarBebidaPorId(bebida1.getId ());
			long bebEliminadas2 = parranderos.eliminarBebidaPorId(bebida2.getId ());
			long bebEliminadas3 = parranderos.eliminarBebidaPorId(bebida3.getId ());
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre (nombreTipoBebida);
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de borrado de las bebidas no servidas 1\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado +=  "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas1);
			resultado += "\n" + listarSirven (listaSirven);
			resultado += "\n\n************ Ejecutando la demo: Borrando bebidas no servidas ************ \n";
			resultado += noServidasEliminadas + " Bebidas eliminadas\n";
			resultado += "\n" + listarBebidas (listaBebidas2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += (bebEliminadas1 + bebEliminadas2 + bebEliminadas3) + " Bebidas eliminadas\n";
			resultado += tbEliminados + " Tipos de bebida eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    */

    /**
     * Demostración de creación y borrado de bebidas no servidas.
     * Incluye también los tipos de bebida pues el tipo de bebida es llave foránea en las bebidas
     * Incluye también los bares pues son llave foránea en la relación Sirven
     * Caso 2: Hay bebidas que son servidas y estas quedan en la tabla de bebidas
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    /*
    public void demoElimNoServidas2 ( )
    {
    	try 
    	{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			String nombreTipoBebida = "Vino tinto";
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida (nombreTipoBebida);
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre (nombreTipoBebida);
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida("120", tipoBebida.getId (), 10);
			VOBebida bebida2 = parranderos.adicionarBebida("Gato Negro", tipoBebida.getId (), 11);
			VOBebida bebida3 = parranderos.adicionarBebida("Don Pedro", tipoBebida.getId (), 12);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogotá", "Bajo", 2);
			parranderos.adicionarSirven (bar1.getId (), bebida1.getId (), "diurno");
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas1 = parranderos.darVOBebidas();
			List <VOBar> bares = parranderos.darVOBares ();
			List <VOSirven> sirven = parranderos.darVOSirven ();
			long noServidasEliminadas = parranderos.eliminarBebidasNoServidas();
			List <VOBebida> listaBebidas2 = parranderos.darVOBebidas();
			
			long sirvenEliminados = parranderos.eliminarSirven(bar1.getId (), bebida1.getId ());
			long bebEliminadas1 = parranderos.eliminarBebidaPorId(bebida1.getId ());
			long bebEliminadas2 = parranderos.eliminarBebidaPorId(bebida2.getId ());
			long bebEliminadas3 = parranderos.eliminarBebidaPorId(bebida3.getId ());
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre (nombreTipoBebida);
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de borrado de las bebidas no servidas 2\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas1);
			resultado += "\n" + listarBares (bares);
			resultado += "\n" + listarSirven (sirven);
			resultado += "\n\n************ Ejecutando la demo: Borrando bebidas no servidas ************ \n";
			resultado += noServidasEliminadas + " Bebidas eliminadas\n";
			resultado += "\n" + listarBebidas (listaBebidas2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += sirvenEliminados + " Sirven eliminados\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += (bebEliminadas1 + bebEliminadas2 + bebEliminadas3) + " Bebidas eliminadas\n";
			resultado += tbEliminados + " Tipos de bebida eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    */
	/* ****************************************************************
	 * 			Demos de LECTOR
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de Bares
     * Incluye también los espacios pues los espacios es llave foránea en los lectores
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    public void demoLector ( )
    {
		try 
		{
			//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			//LECTOR
			VOLECTOR lector= aforo.adicionarLector(espacio.getID_ESPACIO());
			
			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
			List <VOLECTOR> listaLectores= aforo.darVOLector();
			long lectoresEliminados = aforo.eliminarLector(lector.getID_LECTOR());
			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Lector\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
			if (errorEspacio)
			{
				resultado += "*** Exception creando el espacio!!\n";
				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el id del Espacio\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "Adicionado el espacio con id: " + espacio.getID_ESPACIO()+ "\n";
			resultado += "Adicionada el lector con id: " + lector.getID_LECTOR()+ "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarEspacios (listaEspacio);
			resultado += "\n" + listarLectores(listaLectores);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += lectoresEliminados + " Lectores eliminados\n";
			resultado += espaciosEliminados + " Espacios eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
   
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
   	/* ****************************************************************
   	 * 			Demos de LOCAL_COMERCIAL
   	 *****************************************************************/
       /**
        * Demostración de creación, consulta y borrado de Bares
        * Incluye también los espacios pues los espacios es llave foránea en los locales comerciales
        * Muestra la traza de la ejecución en el panelDatos
        * 
        * Pre: La base de datos está vacía
        * Post: La base de datos está vacía
        */
       public void demoLocalComercial ( )
       {
   		try 
   		{
   			//ESPACIO
   			boolean errorEspacio= false;
   			//Creacion fechas a TimeStamp
   			String horarioAperturaEmpleados="2020-12-14 07:00:00";
   			String horarioAperturaClientes="2020-12-14 10:00:00";
   			String horarioCierreClientes = "2020-12-14 18:00:00";
   			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
   			DateFormat formatter = new SimpleDateFormat(FORMAT);
   			
               Date hCC = formatter.parse(horarioCierreClientes);
               Date hAC= formatter.parse(horarioAperturaClientes);
               Date hAE = formatter.parse(horarioAperturaEmpleados);
   			
               Timestamp  ts1 = new Timestamp(hAE.getTime());
               Timestamp  ts2 = new Timestamp(hAC.getTime());
               Timestamp  ts3 = new Timestamp(hCC.getTime());
               //
   			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
   			if (espacio== null)
   			{
   				errorEspacio= true;
   			}
   			//LOCAL_COMERCIAL
   			String area="228.776";
  			float areaReal=Float.parseFloat(area);
   			VOLOCAL_COMERCIAL localComercial= aforo.adicionarLocalComercial(espacio.getID_ESPACIO(), "McDonalds", "McDonalds", areaReal, "Restaurante");
   			
   			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
   			List <VOLOCAL_COMERCIAL> listaLocalesComerciales= aforo.darVOLOCAL_COMERCIAL();
   			long localesEliminados = aforo.eliminarLector(localComercial.getID_LOCAL());
   			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
   			
   			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
   			String resultado = "Demo de creación y listado de Lector\n\n";
   			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
   			if (errorEspacio)
   			{
   				resultado += "*** Exception creando el espacio!!\n";
   				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el id del Espacio\n";
   				resultado += "*** Revise el log de parranderos para más detalles\n";
   			}
   			resultado += "Adicionado el espacio con id: " + espacio.getID_ESPACIO()+ "\n";
   			resultado += "Adicionada el lector con id: " + localComercial.getID_LOCAL()+ "\n";
   			resultado += "\n\n************ Ejecutando la demo ************ \n";
   			resultado +=  "\n" + listarEspacios (listaEspacio);
   			resultado += "\n" + listarLocalComercial(listaLocalesComerciales);
   			resultado += "\n\n************ Limpiando la base de datos ************ \n";
   			resultado += localesEliminados + " Lectores eliminados\n";
   			resultado += espaciosEliminados + " Espacios eliminados\n";
   			resultado += "\n Demo terminada";
      
   			panelDatos.actualizarInterfaz(resultado);
      
   		} 
   		catch (Exception e) 
   		{
//   			e.printStackTrace();
   			String resultado = generarMensajeError(e);
   			panelDatos.actualizarInterfaz(resultado);
   		}
       }
    /**
     * Demostración de la consulta: Dar el id y el número de bebidas que sirve cada bar, siempre y cuando el bar sirva por los menos una bebida
     * Incluye el manejo de los tipos de bebida pues el tipo de bebida es llave foránea en las bebidas
     * Incluye el manajo de las bebidas
     * Incluye el manejo de los bares
     * Incluye el manejo de la relación sirven
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    /*
    public void demoBaresBebidas ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			VOBebida bebida2 = parranderos.adicionarBebida ("121", tipoBebida.getId (), 10);
			VOBebida bebida3 = parranderos.adicionarBebida ("122", tipoBebida.getId (), 10);
			VOBebida bebida4 = parranderos.adicionarBebida ("123", tipoBebida.getId (), 10);
			VOBebida bebida5 = parranderos.adicionarBebida ("124", tipoBebida.getId (), 10);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogotá", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogotá", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogotá", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medellín", "Bajo", 5);
			parranderos.adicionarSirven (bar1.getId (), bebida1.getId (), "diurno");
			parranderos.adicionarSirven (bar1.getId (), bebida2.getId (), "diurno");
			parranderos.adicionarSirven (bar2.getId (), bebida1.getId (), "diurno");
			parranderos.adicionarSirven (bar2.getId (), bebida2.getId (), "diurno");
			parranderos.adicionarSirven (bar2.getId (), bebida3.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida1.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida2.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida3.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida4.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida5.getId (), "diurno");
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida ();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas ();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOSirven> listaSirven = parranderos.darVOSirven ();

			List <long []> listaByB = parranderos.darBaresYCantidadBebidasSirven();

			long sirvenEliminados = parranderos.eliminarSirven (bar1.getId (), bebida1.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar1.getId (), bebida2.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar2.getId (), bebida1.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar2.getId (), bebida2.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar2.getId (), bebida3.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida1.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida2.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida3.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida4.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida5.getId ());
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("121");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("122");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("123");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("124");
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos2");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos3");
			baresEliminados += parranderos.eliminarBarPorId (bar4.getId ());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Bares y cantidad de visitas que reciben\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarSirven (listaSirven);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarBaresYBebidas (listaByB);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += sirvenEliminados + " Sirven eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminados\n";
			resultado += tbEliminados + " Tipos de Bebida eliminados\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
	*/
    
    /**
     * Demostración de la modificación: Aumentar en uno el número de sedes de los bares de una ciudad
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
       /*
    public void demoAumentarSedesBaresEnCiudad ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogotá", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogotá", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogotá", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medellín", "Bajo", 5);
			List <VOBar> listaBares = parranderos.darVOBares ();
			
			long baresModificados = parranderos.aumentarSedesBaresCiudad("Bogotá");
			List <VOBar> listaBares2 = parranderos.darVOBares ();

			long baresEliminados = parranderos.eliminarBarPorId (bar1.getId ());
			baresEliminados += parranderos.eliminarBarPorId (bar2.getId ());
			baresEliminados += parranderos.eliminarBarPorId (bar3.getId ());
			baresEliminados += parranderos.eliminarBarPorId (bar4.getId ());
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo

			String resultado = "Demo de modificación número de sedes de los bares de una ciudad\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += baresModificados + " Bares modificados\n";
			resultado += "\n" + listarBares (listaBares2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
*/
	/* ****************************************************************
	 * 			Demos de VISITANTE
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de Visitantes
     * Incluye también los espacios pues los espacios es llave foránea en los visitantes
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
	public void demoVisitantes ( )
    {
		try 
		{
			//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			//VISITANTE
			String horarioDisponible="2020-12-14 07:00:00";
            Date hDReal = formatter.parse(horarioDisponible);
            Timestamp  avaliable = new Timestamp(hDReal.getTime());
			String cedula="891902912291821";
			float cedulaReal=Float.parseFloat(cedula);
			String telefono="3016977651";
			float telefonoReal=Float.parseFloat(telefono);
			String telefonoContacto="3005675642";
			float telefonoContactoReal=Float.parseFloat(telefonoContacto);
			VOVISITANTE visitante= aforo.adicionarVisitante(cedulaReal, "Danut", telefonoReal, "Danut Mama", telefonoContactoReal, "http://dummyimage.com/131x181.jpg/cc0000/ffffff", "dadadanut@gmail.com",
					avaliable, "Administrador_local", espacio.getID_ESPACIO(),"Verde");
   			
   			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
   			List <VOVISITANTE> listaVisitantes= aforo.darVOBeVisitantes();
   			long visitantesEliminados = aforo.eliminarVisitantePorCedula(visitante.getCEDULA());
   			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
   			
   		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
   			String resultado = "Demo de creación y listado de Visitantes\n\n";
   			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
   			if (errorEspacio)
   			{
   				resultado += "*** Exception creando el espacio!!\n";
   				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el id del Espacio\n";
   				resultado += "*** Revise el log de parranderos para más detalles\n";
   			}
   			resultado += "Adicionado el espacio con id: " + espacio.getID_ESPACIO()+ "\n";
   			resultado += "Adicionada el visitantes con el nombre: " + visitante.getNOMBRE()+ "\n";
   			resultado += "\n\n************ Ejecutando la demo ************ \n";
   			resultado +=  "\n" + listarEspacios (listaEspacio);
   			resultado += "\n" + listarVisitantes(listaVisitantes);
   			resultado += "\n\n************ Limpiando la base de datos ************ \n";
   			resultado += visitantesEliminados + " Lectores eliminados\n";
   			resultado += espaciosEliminados + " Espacios eliminados\n";
   			resultado += "\n Demo terminada";
      
   
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
     * Demostración de creación, consulta de TODA LA INFORMACIÖN de un bebedor y borrado de un bebedor y sus visitas
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de bebidas
     * Incluye el manejo de bares
     * Incluye el manejo de la relación sirven
     * Incluye el manejo de la relación gustan
     * Incluye el borrado de un bebedor y todas sus visitas
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
   /*
	public void demoDarBebedorCompleto ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados.
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogotá", "Bajo", 2);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogotá", "Alto");
			
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTipos = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();

			VOBebedor bdor2 = parranderos.darBebedorCompleto(bdor1.getId ());

			long gustanEliminados = parranderos.eliminarGustan (bdor1.getId (), bebida1.getId ());
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tiposEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long [] bebedorVisitasEliminados = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de toda la información de un bebedor\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTipos);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando toda la información del bebedor con id " + bdor1 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2.toStringCompleto() + "\n" : "Ese bebedor no existe\n";	
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tiposEliminados + " Tipos de Bebida eliminados\n";
			resultado += bebedorVisitasEliminados [0] + " Bebedores eliminados y " + bebedorVisitasEliminados [1] +" Visitas eliminadas\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
}
*/

    /**
     * Demostración de creación, consulta de TODA LA INFORMACIÖN de un bebedor y borrado de un bebedor y sus visitas.
     * Si hay posibilidades de alguna incoherencia con esta operación NO SE BORRA NI EL BEBEDOR NI SUS VISITAS
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de bebidas
     * Incluye el manejo de bares
     * Incluye el manejo de la relación sirven
     * Incluye el manejo de la relación gustan
     * Incluye el borrado de un bebedor y todas sus visitas v1
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos queda con las tuplas que no se pudieron borrar: ES COHERENTE DE TODAS MANERAS
     */
	/*
    public void demoEliminarBebedorYVisitas_v1 ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados.
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogotá", "Alto");
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogotá", "Bajo", 2);
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTipos = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();

			VOBebedor bdor2 = parranderos.darBebedorCompleto(bdor1.getId ());

			// No se elimina la tupla de GUSTAN para estudiar la coherencia de las operaciones en la base de daatos
			long gustanEliminados = 0;
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tiposEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long [] bebedorVisitasEliminados = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de toda la información de un bebedor\n";
			resultado += "Y DE BORRADO DE BEBEDOR Y VISITAS cuando el bebedor aún está referenciado cuando se quiere borrar\n";
			resultado += "v1: No se borra NI el bebedor NI sus visitas";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTipos);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando toda la información del bebedor con id " + bdor1 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2.toStringCompleto() + "\n" : "Ese bebedor no existe\n";	
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tiposEliminados + " Tipos de Bebida eliminados\n";
			resultado += bebedorVisitasEliminados [0] + " Bebedores eliminados y " + bebedorVisitasEliminados [1] +" Visitas eliminadas\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n\n************ ATENCIÓN - ATENCIÓN - ATENCIÓN - ATENCIÓN ************ \n";
			resultado += "\nRecuerde que -1 registros borrados significa que hubo un problema !! \n";
			resultado += "\nREVISE EL LOG DE PARRANDEROS Y EL DE DATANUCLEUS \n";
			resultado += "\nNO OLVIDE LIMPIAR LA BASE DE DATOS \n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
}
*/

    /**
     * Demostración de creación, consulta de TODA LA INFORMACIÖN de un bebedor y borrado de un bebedor y sus visitas
     * Si hay posibilidades de alguna incoherencia con esta operación SE BORRA LO AQUELLO QUE SEA POSIBLE, 
     * PERO CONSERVANDO LA COHERENCIA DE LA BASE DE DATOS
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de bebidas
     * Incluye el manejo de bares
     * Incluye el manejo de la relación sirven
     * Incluye el manejo de la relación gustan
     * Incluye el borrado de un bebedor y todas sus visitas v2
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos queda con las tuplas que no se pudieron borrar
     */
	/*
    public void demoEliminarBebedorYVisitas_v2 ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados.
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogotá", "Alto");
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogotá", "Bajo", 2);
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTipos = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();

			VOBebedor bdor2 = parranderos.darBebedorCompleto(bdor1.getId ());

			// No se elimina la tupla de GUSTAN para estudiar la coherencia de las operaciones en la base de daatos
			long gustanEliminados = 0;
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tiposEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long [] bebedorVisitasEliminados = parranderos.eliminarBebedorYVisitas_v2 (bdor1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de toda la información de un bebedor\n";
			resultado += "Y DE BORRADO DE BEBEDOR Y VISITA,S cuando el bebedor aún está referenciado cuando se quiere borrar\n";
			resultado += "v2: El bebedor no se puede borrar, pero sus visitas SÍ";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTipos);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando toda la información del bebedor con id " + bdor1 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2.toStringCompleto() + "\n" : "Ese bebedor no existe\n";	
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tiposEliminados + " Tipos de Bebida eliminados\n";
			resultado += bebedorVisitasEliminados [0] + " Bebedores eliminados y " + bebedorVisitasEliminados [1] +" Visitas eliminadas\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n\n************ ATENCIÓN - ATENCIÓN - ATENCIÓN - ATENCIÓN ************ \n";
			resultado += "\nRecuerde que -1 registros borrados significa que hubo un problema !! \n";
			resultado += "\nREVISE EL LOG DE PARRANDEROS Y EL DE DATANUCLEUS \n";
			resultado += "\nNO OLVIDE LIMPIAR LA BASE DE DATOS \n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
}
*/
    /**
     * Demostración de la modificación: Cambiar la ciudad de un bebedor
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
    */
/*
    public void demoCambiarCiudadBebedor ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogotá", "Alto");
			
			List<VOBebedor> bebedores1 = parranderos.darVOBebedores ();
			long bebedoresActualizados = parranderos.cambiarCiudadBebedor (bdor1.getId (), "Medellín");
			List<VOBebedor> bebedores2 = parranderos.darVOBebedores ();
			
			long bebedoresEliminados = parranderos.eliminarBebedorPorNombre ("Pepito");

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de modificación de la ciudad de un bebedor\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBebedores (bebedores1);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += bebedoresActualizados + " Bebedores modificados\n";
			resultado += "\n" + listarBebedores (bebedores2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += bebedoresEliminados + " Bebedores eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
*/
    /**
     * Demostración de la consulta: Dar la información de los bebedores y del número de bares que visita cada uno
     * Incluye el manejo de los bares
     * Incuye el manejo de la relación visitan
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
	/*
    public void demoBebedoresYNumVisitasRealizadas ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogotá", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogotá", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogotá", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medellín", "Bajo", 5);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogotá", "Alto");
			VOBebedor bdor2 = parranderos.adicionarBebedor ("Juanito", "Bogotá", "Alto");
			VOBebedor bdor3 = parranderos.adicionarBebedor ("Carlitos", "Medellín", "Alto");
			VOBebedor bdor4 = parranderos.adicionarBebedor ("Luis", "Cartagena", "Medio");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarVisitan (bdor1.getId (), bar2.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor2.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor2.getId (), bar4.getId (), new Timestamp (System.currentTimeMillis()), "diurno");

			List<VOBar> bares = parranderos.darVOBares();
			List<VOBebedor> bebedores = parranderos.darVOBebedores();
			List<VOVisitan> visitan = parranderos.darVOVisitan ();
			List<Object []> bebedoresYNumVisitas = parranderos.darBebedoresYNumVisitasRealizadas ();

			long [] elimBdor1 = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long [] elimBdor2 = parranderos.eliminarBebedorYVisitas_v1 (bdor2.getId ());
			long [] elimBdor3 = parranderos.eliminarBebedorYVisitas_v1 (bdor3.getId ());
			long [] elimBdor4 = parranderos.eliminarBebedorYVisitas_v1 (bdor4.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos2");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos3");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos4");

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de dar bebedores y cuántas visitan han realizado\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (bares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarVisitan (visitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarBebedorYNumVisitas (bebedoresYNumVisitas);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += elimBdor1 [0] + " Bebedores eliminados y " + elimBdor1 [1] +" Visitas eliminadas\n";
			resultado += elimBdor2 [0] + " Bebedores eliminados y " + elimBdor2 [1] +" Visitas eliminadas\n";
			resultado += elimBdor3 [0] + " Bebedores eliminados y " + elimBdor3 [1] +" Visitas eliminadas\n";
			resultado += elimBdor4 [0] + " Bebedores eliminados y " + elimBdor4 [1] +" Visitas eliminadas\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
*/
    /**
     * Demostración de la consulta: Para cada ciudad, cuántos bebedores vistan bares
     * Incluye el manejo de bares
     * Incluye el manejo de la relación visitan
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
	/*
    public void demoBebedoresDeCiudad ( )
    {
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogotá", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogotá", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogotá", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medellín", "Bajo", 5);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogotá", "Alto");
			VOBebedor bdor2 = parranderos.adicionarBebedor ("Juanito", "Medellín", "Alto");
			VOBebedor bdor3 = parranderos.adicionarBebedor ("Pedrito", "Medellín", "Alto");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar2.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor2.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar4.getId (), new Timestamp (System.currentTimeMillis()), "diurno");

			List<VOBar> bares = parranderos.darVOBares();
			List<VOBebedor> bebedores = parranderos.darVOBebedores();
			List<VOVisitan> visitan = parranderos.darVOVisitan ();
			long bebedoresBogota = parranderos.darCantidadBebedoresCiudadVisitanBares ("Bogotá");
			long bebedoresMedellin = parranderos.darCantidadBebedoresCiudadVisitanBares ("Medellín");

			long [] elimBdor1 = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long [] elimBdor2 = parranderos.eliminarBebedorYVisitas_v1 (bdor2.getId ());
			long elimBdor3 = parranderos.eliminarBebedorPorId (bdor3.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos2");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos3");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos4");

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de dar cantidad de bebedores de una ciudad que vistan baresn\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (bares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarVisitan (visitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBebedores de Bogotá: " + bebedoresBogota;
			resultado += "\nBebedores de Medellín: " + bebedoresMedellin;
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += elimBdor1 [0] + " Bebedores eliminados y " + elimBdor1 [1] +" Visitas eliminadas\n";
			resultado += elimBdor2 [0] + " Bebedores eliminados y " + elimBdor2 [1] +" Visitas eliminadas\n";
			resultado += elimBdor3 + " Bebedores eliminados\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";

			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
*/
	/* ****************************************************************
	 * 			Demos de CARNET
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de CARNET
     * Muestra la traza de la ejecución en el panelDatos
     * Incluye también los visitantes pues los visitantes son llave foránea en los carnets
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    public void demoCarnet( )
    {
		try 
		{
			//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			//VISITANTE
			String horarioDisponible="2020-12-14 07:00:00";
            Date hDReal = formatter.parse(horarioDisponible);
            Timestamp  avaliable = new Timestamp(hDReal.getTime());
			String cedula="891902912291821";
			float cedulaReal=Float.parseFloat(cedula);
			String telefono="3016977651";
			float telefonoReal=Float.parseFloat(telefono);
			String telefonoContacto="3005675642";
			float telefonoContactoReal=Float.parseFloat(telefonoContacto);
			VOVISITANTE visitante= aforo.adicionarVisitante(cedulaReal, "Danut", telefonoReal, "Danut Mama", telefonoContactoReal, "http://dummyimage.com/131x181.jpg/cc0000/ffffff", "dadadanut@gmail.com",
					avaliable, "Administrador_local", espacio.getID_ESPACIO(),"Verde");
   			//CARNET
			VOCARNET carnet=aforo.adicionarCarnet(visitante.getCEDULA());
			
			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
   			List <VOVISITANTE> listaVisitantes= aforo.darVOBeVisitantes();
   			List <VOCARNET> listaCarnet=aforo.darVOCarnets();
   			long visitantesEliminados = aforo.eliminarVisitantePorCedula(visitante.getCEDULA());
   			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
   			long carnetsEliminados=aforo.eliminarCarnetPorId(carnet.getID_CARNET());
   
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Gustan\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorEspacio)
			{
				resultado += "*** Exception creando Espacio!!\n";
				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarEspacios(listaEspacio);
			resultado += "\n" + listarVisitantes (listaVisitantes);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarCarnets(listaCarnet);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += espaciosEliminados + " Espacios eliminados\n";
			resultado += visitantesEliminados+ " Visitantes eliminadas\n";
			resultado += carnetsEliminados + " Carnets eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			Demos de VISITA
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de la relación Visita
     * Incluye el manejo de Espacio
     * Incluye el manejo de Lector
     * Incluye el manejo de Visitante
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    public void demoVisita( )
    {
		try 
		{
			//ESPACIO
			boolean errorEspacio= false;
			//Creacion fechas a TimeStamp
			String horarioAperturaEmpleados="2020-12-14 07:00:00";
			String horarioAperturaClientes="2020-12-14 10:00:00";
			String horarioCierreClientes = "2020-12-14 18:00:00";
			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
			DateFormat formatter = new SimpleDateFormat(FORMAT);
			
            Date hCC = formatter.parse(horarioCierreClientes);
            Date hAC= formatter.parse(horarioAperturaClientes);
            Date hAE = formatter.parse(horarioAperturaEmpleados);
			
            Timestamp  ts1 = new Timestamp(hAE.getTime());
            Timestamp  ts2 = new Timestamp(hAC.getTime());
            Timestamp  ts3 = new Timestamp(hCC.getTime());
            //
			VOESPACIO espacio= aforo.adicionarEspacio(ts1, ts2, ts3, 10, 100,"Verde");
			if (espacio== null)
			{
				errorEspacio= true;
			}
			//LECTOR
			VOLECTOR lector= aforo.adicionarLector(espacio.getID_ESPACIO());
			//VISITANTE
			String horarioDisponible="2020-12-14 07:00:00";
            Date hDReal = formatter.parse(horarioDisponible);
            Timestamp  avaliable = new Timestamp(hDReal.getTime());
			String cedula="891902912291821";
			float cedulaReal=Float.parseFloat(cedula);
			String telefono="3016977651";
			float telefonoReal=Float.parseFloat(telefono);
			String telefonoContacto="3005675642";
			float telefonoContactoReal=Float.parseFloat(telefonoContacto);
			VOVISITANTE visitante= aforo.adicionarVisitante(cedulaReal, "Danut", telefonoReal, "Danut Mama", telefonoContactoReal, "http://dummyimage.com/131x181.jpg/cc0000/ffffff", "dadadanut@gmail.com",
					avaliable, "Administrador_local", espacio.getID_ESPACIO(),"Verde");
   			//CARNET
			VOCARNET carnet=aforo.adicionarCarnet(visitante.getCEDULA());
			//VISITA
			String horaOperacion="2020-12-14 12:00:00";
            Date hOReal = formatter.parse(horaOperacion);
            Timestamp  xs= new Timestamp(hOReal.getTime());
            String horaOperacion2="2020-12-14 14:00:00";
            Date hOReal2 = formatter.parse(horaOperacion2);
            Timestamp  xs2= new Timestamp(hOReal2.getTime());
   			VOVISITA visita=aforo.adicionarVisita(xs, "Salida",xs2, carnet.getID_CARNET(), lector.getID_LECTOR(), espacio.getID_ESPACIO());
			
   			List <VOESPACIO> listaEspacio= aforo.darVOBEspacios();
			List <VOLECTOR> listaLectores= aforo.darVOLector();
			List <VOCARNET> listaCarnet=aforo.darVOCarnets();
   			List <VOVISITANTE> listaVisitantes= aforo.darVOBeVisitantes();
   			List <VOVISITA> listaVisitas= aforo.darVOVisita();
   			
			long lectoresEliminados = aforo.eliminarLector(lector.getID_LECTOR());
			long espaciosEliminados = aforo.eliminarEspacioPorId(espacio.getID_ESPACIO());
   			long carnetsEliminados=aforo.eliminarCarnetPorId(carnet.getID_CARNET());
   			long visitantesEliminados = aforo.eliminarVisitantePorCedula(visitante.getCEDULA());
   			long visitasEliminadas= aforo.eliminarVisita(visita.getIDCARNET(), visita.getIDLECTOR(), visita.getIDESPACIO());
   				
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Visita\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorEspacio)
			{
				resultado += "*** Exception creando Espacio!!\n";
				resultado += "*** Es probable que ese espacio ya existiera y hay restricción de UNICIDAD sobre el nombre del ID\n";
				resultado += "*** Revise el log de parranderos para más detalles\n";
			}
			resultado += "\n" + listarEspacios(listaEspacio);
			resultado += "\n" + listarLectores (listaLectores);
			resultado += "\n" + listarCarnets (listaCarnet);
			resultado += "\n" + listarVisitantes(listaVisitantes);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarVisitas(listaVisitas);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += lectoresEliminados + " Lectores eliminados\n";
			resultado += espaciosEliminados + " Espacios eliminados\n";
			resultado += carnetsEliminados + " Carnets eliminados\n";
			resultado += visitantesEliminados + " Visitantes eliminados\n";
			resultado += visitasEliminadas + " Visitas eliminadas\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
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
			resultado += eliminados [0] + " Baños eliminados\n";
			resultado += eliminados [1] + " Parqueaderos eliminados\n";
			resultado += eliminados [2] + " Visitas eliminadas\n";
			resultado += eliminados [3] + " Visitantes eliminados\n";
			resultado += eliminados [4] + " Espacios eliminados\n";
			resultado += eliminados [5] + " Lectores eliminados\n";
			resultado += eliminados [6] + " Carnets eliminados\n";
			resultado += eliminados [7] + " Locales comerciales eliminados\n";
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
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
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
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una líea para cada tipo de bebida recibido
     */
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

    /**
     * Genera una cadena de caracteres con la lista de bebidas recibida: una línea por cada bebida
     * @param lista - La lista con las bebidas
     * @return La cadena con una líea para cada bebida recibida
     */
    private String listarParqueaderos (List<VOPARQUEADERO> lista) 
    {
    	String resp = "Las parqueaderos existentes son:\n";
    	int i = 1;
        for (VOPARQUEADERO beb : lista)
        {
        	resp += i++ + ". " + beb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de bebedores recibida: una línea por cada bebedor
     * @param lista - La lista con los bebedores
     * @return La cadena con una líea para cada bebedor recibido
     */
    private String listarLocalComercial(List<VOLOCAL_COMERCIAL> lista) 
    {
    	String resp = "Los locales comerciales existentes son:\n";
    	int i = 1;
        for (VOLOCAL_COMERCIAL bdor : lista)
        {
        	resp += i++ + ". " + bdor.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de bares recibida: una línea por cada bar
     * @param lista - La lista con los bares
     * @return La cadena con una líea para cada bar recibido
     */
    private String listarLectores(List<VOLECTOR> lista) 
    {
    	String resp = "Los lectores existentes son:\n";
    	int i = 1;
        for (VOLECTOR lector: lista)
        {
        	resp += i++ + ". " + lector.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de gustan recibida: una línea por cada gusta
     * @param lista - La lista con los gustan
     * @return La cadena con una líea para cada gustan recibido
     */
    private String listarVisitantes(List<VOVISITANTE> lista) 
    {
    	String resp = "Los gustan existentes son:\n";
    	int i = 1;
        for (VOVISITANTE serv : lista)
        {
        	resp += i++ + ". " + serv.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de sirven recibida: una línea por cada sirven
     * @param lista - La lista con los sirven
     * @return La cadena con una líea para cada sirven recibido
     */
    private String listarCarnets(List<VOCARNET> lista) 
    {
    	String resp = "Los carnets existentes son:\n";
    	int i = 1;
        for (VOCARNET serv : lista)
        {
        	resp += i++ + ". " + serv.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de visitan recibida: una línea por cada visitan
     * @param lista - La lista con los visitan
     * @return La cadena con una líea para cada visitan recibido
     */
    private String listarVisitas (List<VOVISITA> lista) 
    {
    	String resp = "Los visitan existentes son:\n";
    	int i = 1;
        for (VOVISITA vis : lista)
        {
        	resp += i++ + ". " + vis.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de parejas de números recibida: una línea por cada pareja
     * @param lista - La lista con las pareja
     * @return La cadena con una líea para cada pareja recibido
     */
    private String listarBaresYBebidas (List<long[]> lista) 
    {
    	String resp = "Los bares y el número de bebidas que sirven son:\n";
    	int i = 1;
        for ( long [] tupla : lista)
        {
			long [] datos = tupla;
	        String resp1 = i++ + ". " + "[";
			resp1 += "idBar: " + datos [0] + ", ";
			resp1 += "numBebidas: " + datos [1];
	        resp1 += "]";
	        resp += resp1 + "\n";
        }
        return resp;
	}
    /*
    /**
     * Genera una cadena de caracteres con la lista de parejas de objetos recibida: una línea por cada pareja
     * @param lista - La lista con las parejas (Bebedor, numero visitas)
     * @return La cadena con una línea para cada pareja recibido
     /*
    private String listarBebedorYNumVisitas (List<Object[]> lista) 
    {
    	String resp = "Los bebedores y el número visitas realizadas son:\n";
    	int i = 1;
        for (Object [] tupla : lista)
        {
			VOBebedor bdor = (VOBebedor) tupla [0];
			int numVisitas = (int) tupla [1];
	        String resp1 = i++ + ". " + "[";
			resp1 += bdor + ", ";
			resp1 += "numVisitas: " + numVisitas;
	        resp1 += "]";
	        resp += resp1 + "\n";
        }
        return resp;
	}
    */
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
			Method req = InterfazParranderosDemo.class.getMethod ( evento );			
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
            InterfazParranderosDemo interfaz = new InterfazParranderosDemo( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
