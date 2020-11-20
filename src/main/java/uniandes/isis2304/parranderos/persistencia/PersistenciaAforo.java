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

package uniandes.isis2304.parranderos.persistencia;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import oracle.sql.TIMESTAMP;
import uniandes.isis2304.parranderos.negocio.BAÑO;
import uniandes.isis2304.parranderos.negocio.CARNET;
import uniandes.isis2304.parranderos.negocio.ESPACIO;
import uniandes.isis2304.parranderos.negocio.LECTOR;
import uniandes.isis2304.parranderos.negocio.LOCAL_COMERCIAL;
import uniandes.isis2304.parranderos.negocio.PARQUEADERO;
import uniandes.isis2304.parranderos.negocio.VISITA;
import uniandes.isis2304.parranderos.negocio.VISITANTE;

/**
 * Clase para el manejador de persistencia del proyecto AforoCCAndes
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * Se apoya en las clases SQLBAÑO, SQLCARNET, SQLCENTRO_COMERCIAL, SQLESPACIO, SQLLECTOR, SQLLOCAL_COMERCIAL, SQLPARQUEADERO,
 * SQLVISITA, SQLVISITANTE que son las que realizan el acceso a la base de datos
 * 
 * 
 */
public class PersistenciaAforo 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaAforo.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaAforo instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, baño, carnet, centro_comercial, espacio, lector, local_comercial,
	 * parqueadero, visita y visitante.
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	/**
	 * Atributo para el acceso a la tabla PARQUEADERO de la base de datos
	 */
	private SQLPARQUEADERO sqlParqueadero;
	
	/**
	 * Atributo para el acceso a la tabla LECTOR de la base de datos
	 */
	private SQLLECTOR sqlLector;
	
	/**
	 * Atributo para el acceso a la tabla LOCALCOMERCIAL de la base de datos
	 */
	private SQLLOCAL_COMERCIAL sqlLocalComercial;
	
	/**
	 * Atributo para el acceso a la tabla BAÑO de la base de datos
	 */
	private SQLBAÑO sqlBaño;

	/**
	 * Atributo para el acceso a la tabla CARNET de la base de datos
	 */
	private SQLCARNET sqlCarnet;
	
	/**
	 * Atributo para el acceso a la tabla CENTRO_COMERCIAL de la base de datos
	 */
	private SQLCENTRO_COMERCIAL sqlCentroComercial;
	
	/**
	 * Atributo para el acceso a la tabla ESPACIO de la base de datos
	 */
	private SQLESPACIO sqlEspacio;
	
	/**
	 * Atributo para el acceso a la tabla VISITA de la base de datos
	 */
	private SQLVISITA sqlVisita;
	/**
	 * Atributo para el acceso a la tabla VISITANTE de la base de datos
	 */
	private SQLVISITANTE sqlVisitante;
	
	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAforo ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("centro_comercial_sequence");
		tablas.add ("BAÑO");
		tablas.add ("CARNET");
		tablas.add ("CENTRO_COMERCIAL");
		tablas.add ("ESPACIO");
		tablas.add ("LECTOR");
		tablas.add ("LOCAL_COMERCIAL");
		tablas.add ("PARQUEADERO");
		tablas.add ("VISITA");
		tablas.add ("VISITANTE");
}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaAforo (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAforo getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaAforo ();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAforo getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaAforo (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}
	
	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlLector= new SQLLECTOR(this);
		sqlLocalComercial = new SQLLOCAL_COMERCIAL(this);
		sqlBaño = new SQLBAÑO(this);
		sqlCarnet = new SQLCARNET(this);
		sqlCentroComercial = new SQLCENTRO_COMERCIAL(this);
		sqlEspacio = new SQLESPACIO (this);
		sqlVisita = new SQLVISITA(this);	
		sqlParqueadero= new SQLPARQUEADERO(this);
		sqlVisitante=new SQLVISITANTE(this);
		sqlUtil = new SQLUtil(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de parranderos
	 */
	public String darSeqAforo()
	{
		return tablas.get (0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de BAÑO de Aforo
	 */
	public String darTablaBAÑO ()
	{
		return tablas.get (1);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de CARNET de Aforo
	 */
	public String darTablaCARNET()
	{
		return tablas.get (2);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de CENTRO_COMERCIAL de Aforo
	 */
	public String darTablaCENTRO_COMERCIAL ()
	{
		return tablas.get (3);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ESPACIO de Aforo
	 */
	public String darTablaESPACIO()
	{
		return tablas.get (4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de LECTOR de Aforo
	 */
	public String darTablaLECTOR ()
	{
		return tablas.get (5);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de LOCAL_COMERCIAL de Aforo
	 */
	public String darTablaLOCAL_COMERCIAL ()
	{
		return tablas.get (6);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de PARQUEADERO de Aforo
	 */
	public String darTablaPARQUEADERO ()
	{
		return tablas.get (7);
	}
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de VISITA de Aforo
	 */
	public String darTablaVISITA()
	{
		return tablas.get (8);
	}
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de VISITANTE de Aforo
	 */
	public String darTablaVISITANTE()
	{
		return tablas.get (9);
	}
	
	/**
	 * Transacción para el generador de secuencia de Aforo
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de Aforo
	 */
	private long nextval ()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }
	
	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
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

	/* ****************************************************************
	 * 			Métodos para manejar los PARQUEADEROS
	 *****************************************************************/
	public PARQUEADERO adicionarParqueadero(long idEspacio,float capacidad)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idParqueadero = nextval ();
            long tuplasInsertadas = sqlParqueadero.adicionarParqueadero(pm, idEspacio, idParqueadero, capacidad);
            tx.commit();
            log.trace ("Inserción de parqueadero: " + idEspacio+ ","+idParqueadero+": " 
            + tuplasInsertadas + " tuplas insertadas");
            
            return new PARQUEADERO (idEspacio,idParqueadero,capacidad);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarParqueaderoPorId(Long idParqueadero) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlParqueadero.eliminarParqueaderoPorId(pm, idParqueadero);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public PARQUEADERO darParqueaderoPorId (long idParqueadero)
	{
		return sqlParqueadero.darParqueaderoPorId(pmf.getPersistenceManager(), idParqueadero);
	}
	
	public List<PARQUEADERO> darParqueaderos ()
	{
		return sqlParqueadero.darParqueaderos(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar el CENTRO_COMERCIAL
	 *****************************************************************/
	public long aumentarAforoCC()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCentroComercial.aumentarAforoEnElCC(pm);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long cambiarNombreCC(String nombre)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCentroComercial.cambiarNombre(pm, nombre);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	/* ****************************************************************
	 * 			Métodos para manejar los CARNET
	 *****************************************************************/
	public CARNET adicionarCarnet(float cedula) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idCarnet = nextval ();
            long tuplasInsertadas = sqlCarnet.adicionarCarnet(pm, idCarnet, cedula);
            tx.commit();

            log.trace ("Inserción de carnet: " + idCarnet + " .Cedula:" +cedula+ " : " + tuplasInsertadas + " tuplas insertadas");
            
            return new CARNET (idCarnet,cedula);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarCarnetPorId (long idCarnet) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCarnet.eliminarCarnetPorId(pm, idCarnet);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public CARNET darCarnetPorId (long idCarnet) 
	{
		return (CARNET) sqlCarnet.darCarnetPorId(pmf.getPersistenceManager(), idCarnet);
	}
	public List<CARNET> darCarnets ()
	{
		return sqlCarnet.darCarnets(pmf.getPersistenceManager());
	}
	public CARNET cambiarCedulaCarnet(long idCarnet, float cedula)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            CARNET resp = sqlCarnet.cambiarCedula(pm, idCarnet, cedula);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	/* ****************************************************************
	 * 			Métodos para manejar los BAÑOS
	 *****************************************************************/

	public BAÑO adicionarBaño(long idEspacio, int numSanitarios) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idBaño = nextval ();
            long tuplasInsertadas = sqlBaño.adicionarBaño(pm, idEspacio, idBaño, numSanitarios);
            tx.commit();

            log.trace ("Inserción de Baño: " + idBaño+ " IdEspacio: " + idEspacio+ " Número sanitarios: " + numSanitarios+
            		": " + tuplasInsertadas + " tuplas insertadas");

            return new BAÑO (idEspacio, idBaño, numSanitarios);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarBañoPorId (long idBaño) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlBaño.eliminarBañosPorId(pm, idBaño);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla BAR
	 * @return La lista de objetos BAR, construidos con base en las tuplas de la tabla BAR
	 */
	public List<BAÑO> darBaños ()
	{
		return sqlBaño.darBaños(pmf.getPersistenceManager());
	}
	public BAÑO darBañoPorId (long idBaño)
	{
		return sqlBaño.darBañoPorId(pmf.getPersistenceManager(), idBaño);
	}
	public long aumentarAforoBaño(long idBaño)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlBaño.aumentarAforoEnElBaño(pm, idBaño);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	/* ****************************************************************
	 * 			Métodos para manejar la relación ESPACIO
	 *****************************************************************/
	public ESPACIO adicionarEspacio( Timestamp horarioAperturaEmpleados, Timestamp horarioAperturaClientes,
			Timestamp horarioCierreClientes, int aforoActual, int aforoTotal, String estado) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idEspacio = nextval ();
            long tuplasInsertadas = sqlEspacio.adicionarEspacio(pm, idEspacio, horarioAperturaEmpleados, horarioAperturaClientes, horarioCierreClientes, aforoActual, aforoTotal,estado);
            tx.commit();

            log.trace ("Inserción de espacio: [" + idEspacio+ ", " 
            		+ horarioAperturaEmpleados+ ", " + horarioAperturaClientes+ ", " + horarioCierreClientes+ ", " + aforoTotal+ ", "+ estado+"]. " + tuplasInsertadas + " tuplas insertadas");

            return new ESPACIO (idEspacio, horarioAperturaEmpleados, horarioAperturaClientes,
        			horarioCierreClientes, aforoActual, aforoTotal, estado);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarEspacioPorId(long idEspacio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlEspacio.eliminarEspacioPorId(pm, idEspacio);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla GUSTAN
	 * @return La lista de objetos GUSTAN, construidos con base en las tuplas de la tabla GUSTAN
	 */
	public List<ESPACIO> darEspacios()
	{
		return sqlEspacio.darEspacios(pmf.getPersistenceManager());
	}
	public long aumentarAforoEspacio(long idEspacio)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlEspacio.aumentarAforoEnElEspacio(pm, idEspacio);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public List<ESPACIO> darEspaciosPorAforo(int aforoTotal) 
	{
		return sqlEspacio.darEspaciosPorAforo(pmf.getPersistenceManager(), aforoTotal);
	}
	public List<Object[]> RFC3AdminEstablecimiento(long idEspacio)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object[]> respuesta= new LinkedList<Object []> ();
			List<Object> resultado=sqlEspacio.RFC3AdminEstablecimiento(pm, idEspacio);
			for(Object xd: resultado)
			{
				Object [] datos = (Object []) xd;
				float indiceAforoEnCentroComercial= ((BigDecimal) datos [0]).floatValue();
				long idEspacioXd= ((BigDecimal) datos [1]).longValue();
				float indiceAforoEspacio= ((BigDecimal) datos [2]).floatValue();
				
				Object [] resp= new Object[3];
				resp[0]="Indice total Aforo del centro comercial: "+indiceAforoEnCentroComercial+"%";
				resp[1]="Id espacio: "+idEspacioXd;
				resp[2]="Indice del establecimiento: "+indiceAforoEspacio+"%";
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC3AdminCentro(long idEspacio)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object[]> respuesta= new LinkedList<Object []> ();
			List<Object> resultado=sqlEspacio.RFC3AdminCentroPorIdEspacio(pm, idEspacio);
			for(Object xd: resultado)
			{
				Object [] datos = (Object []) xd;
				float indiceAforoEnCentroComercial= ((BigDecimal) datos [0]).floatValue();
				long idEspacioXd= ((BigDecimal) datos [1]).longValue();
				float indiceAforoEspacio= ((BigDecimal) datos [2]).floatValue();
				
				Object [] resp= new Object[3];
				resp[0]="Indice total Aforo del centro comercial: "+indiceAforoEnCentroComercial+"%";
				resp[1]="Id espacio: "+idEspacioXd;
				resp[2]="Indice del establecimiento: "+indiceAforoEspacio+"%";
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC3AdminCentroPorTipo(String tipo)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object[]> respuesta= new LinkedList<Object []> ();
			List<Object> resultado=sqlEspacio.RFC3AdminCentroPorTipoEstablecimiento(pm, tipo);
			for(Object xd: resultado)
			{
				Object [] datos = (Object []) xd;
				float indiceAforoEnCentroComercial= ((BigDecimal) datos [0]).floatValue();
				String tipoEspacio= (String) datos [1];
				float indiceAforoEspacio= ((BigDecimal) datos [2]).floatValue();
				
				Object [] resp= new Object[3];
				resp[0]="Indice total Aforo del centro comercial: "+indiceAforoEnCentroComercial+"%";
				resp[1]="Tipo espacio: "+tipoEspacio;
				resp[2]="Indice del establecimiento: "+indiceAforoEspacio+"%";
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC4()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object[]> respuesta= new LinkedList<Object []> ();
			List<Object> resultado=sqlEspacio.RFC4(pm);
			for(Object xd: resultado)
			{
				Object [] datos = (Object []) xd;
				long idEspacio=((BigDecimal) datos[0]).longValue();
				int aforoActual= ((BigDecimal) datos [1]).intValue();
				int aforoTotal= ((BigDecimal) datos [2]).intValue();
				String estado= (String) datos [3];
				
				Object [] resp= new Object[4];
				resp[0]="[Id espacio: "+idEspacio;
				resp[1]="Aforo actual: "+aforoActual;
				resp[2]="Aforo maximo: "+aforoTotal;
				resp[3]="Estado: "+estado+"]";
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public void RF9(String estado, long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			sqlEspacio.RF9RegistrarCambioDeEstado(pm, estado, id);
		}catch(Exception e)
        {
      	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public void RF11(String estado, long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			sqlEspacio.RF11DeshabilitarTipoDeEspacio(pm, estado, id);
		}catch(Exception e)
        {
      	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public void RF12( long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			sqlEspacio.RF12RehabilitarTipoDeEspacio(pm, id);
		}catch(Exception e)
        {
      	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	/* ****************************************************************
	 * 			Métodos para manejar la relación LOCAL COMERCIAL
	 *****************************************************************/
	public LOCAL_COMERCIAL adicionarLocalComercial(long idEspacio, String nombre, String nombre_empresa, float area, String tipo_establecimiento) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long id_local=nextval();
            long tuplasInsertadas = sqlLocalComercial.adicionarLocalComercial(pm, idEspacio, id_local, nombre, nombre_empresa, area, tipo_establecimiento);
    		tx.commit();

            log.trace ("Inserción de espacio: [" + idEspacio+ ", " 
            		+ id_local+ ", " + nombre+ ", " + nombre_empresa+ ", " + area+", " + tipo_establecimiento+ "]. " 
            		+ tuplasInsertadas + " tuplas insertadas");

            return new LOCAL_COMERCIAL (idEspacio, id_local, nombre, nombre_empresa, area, tipo_establecimiento);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarLocalComercialPorNombre(String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
	        Transaction tx=pm.currentTransaction();
	        try
	        {
	            tx.begin();
	            long resp = sqlLocalComercial.eliminarLocalComercialPorNombre(pm, nombre);
	            tx.commit();

	            return resp;
	        }
	        catch (Exception e)
	        {
//	        	e.printStackTrace();
	        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        	return -1;
	        }
	        finally
	        {
	            if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
	        }
	}
	public long eliminarLocalComercialPorId(long idLocal) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
	        Transaction tx=pm.currentTransaction();
	        try
	        {
	            tx.begin();
	            long resp = sqlLocalComercial.eliminarLocalComercialPorId(pm, idLocal);
	            tx.commit();

	            return resp;
	        }
	        catch (Exception e)
	        {
//	        	e.printStackTrace();
	        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        	return -1;
	        }
	        finally
	        {
	            if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
	        }
	}
	public List<LOCAL_COMERCIAL> darLocalesComerciales()
	{
		return sqlLocalComercial.darLocalesComerciales(pmf.getPersistenceManager());
	}
	public List<LOCAL_COMERCIAL> darLocalComercialPorNombre (String nombreLocal) 
	{
		return sqlLocalComercial.darLocalComercialPorNombre(pmf.getPersistenceManager(), nombreLocal);
	}
	public LOCAL_COMERCIAL darLocalComercialPorId(long idLocal) 
	{
		return sqlLocalComercial.darLocalComercialPorId(pmf.getPersistenceManager(), idLocal);
	}
	public List<Object[]> RFC5PorTipoLocal(String tipo, Timestamp fechaInicio, Timestamp fechaFin)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> locales=sqlLocalComercial.RFC5PorTipoLocal(pm, tipo, fechaInicio, fechaFin);
			for(Object local: locales)
			{
				Object [] datos = (Object []) local;
				long idEspacio= ((BigDecimal) datos [0]).longValue ();;
				String nombreEstablecimiento= (String) datos [1];
				int contadorVisitas= ((BigDecimal) datos [2]).intValue();
				
				Object [] resp = new Object [3];
				resp[0]="[Id Espacio: "+idEspacio;
				resp[1]=nombreEstablecimiento;
				resp[2]=contadorVisitas;
				
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC8(String nombre)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> locales=sqlLocalComercial.RFC8(pm, nombre);
			for(Object local: locales)
			{
				Object [] datos = (Object []) local;
				String nombreVisitante= (String) datos [0];
				float telefono= ((BigDecimal) datos [1]).floatValue ();;
				String correo= (String) datos [2];
				long idCarnetUsuario= ((BigDecimal) datos [3]).longValue ();;
				int contadorVisitas= ((BigDecimal) datos [4]).intValue();
				int mes= ((BigDecimal) datos [5]).intValue();
				
				Object [] resp = new Object [5];
				resp[0]="[Nombre: "+nombreVisitante;
				resp[1]="Telefono: "+telefono;
				resp[2]="Correo: "+correo;
				resp[3]="Id Carnet en ese momento: "+idCarnetUsuario;
				resp[4]="Numero de visitas en el mes "+mes+" : "+contadorVisitas+"]";
				
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	/* ****************************************************************
	 * 			Métodos para manejar la relación VISITA
	 *****************************************************************/
	public VISITA adicionarVisita (Timestamp fechaYHora_op , String tipo_op, Timestamp horafin_op,long IDCARNET, long IDLECTOR,long IDESPACIO) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlVisita.adicionarVisita(pm, fechaYHora_op, tipo_op, horafin_op, IDCARNET, IDLECTOR, IDESPACIO);
            tx.commit();

            log.trace ("Inserción de visita: [" + fechaYHora_op+ ", " 
            		+ tipo_op+ ", " + horafin_op+ ", " + IDCARNET+ ", " + IDLECTOR+", " + IDESPACIO+ "]. " 
            		+ tuplasInsertadas + " tuplas insertadas");

            return new VISITA (fechaYHora_op, tipo_op, horafin_op, IDCARNET,IDLECTOR,IDESPACIO);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarVisita(long idCarnet, long idLector, long idEspacio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlVisita.eliminarVisita(pm, idCarnet, idLector, idEspacio);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarVisitasPorIdLector(long idLector) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long visitasEliminadas = sqlVisita.eliminarVisitasPorIdLector(pm, idLector);
            tx.commit();

            return visitasEliminadas;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarVisitasPorIdEspacio (long idEspacio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long visitasEliminadas = sqlVisita.eliminarVisitasPorIdEspacio(pm, idEspacio);
            tx.commit();

            return visitasEliminadas;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public List<VISITA> darVisitas ()
	{
		return sqlVisita.darVisitas(pmf.getPersistenceManager());
	}	
	public List<Object[]> RFC1AdminEstablecimiento(long idEspacio, Timestamp fechaInicio, Timestamp fechaFin)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> visitantesAtendidos=sqlVisita.RFC1AdminEstablecimiento(pm, idEspacio, fechaInicio, fechaFin);
			for(Object visitante: visitantesAtendidos)
			{
				Object [] datos = (Object []) visitante;
				long idCarnet = ((BigDecimal) datos [0]).longValue ();
				long cedula = ((BigDecimal) datos [1]).longValue ();
				String nombreVisitante= (String) datos [2];
				float telefonoVisitante= ((BigDecimal) datos [3]).floatValue();
				String nombreContactoVisitante= (String) datos [4];
				float telefonoContactoVisitante= ((BigDecimal) datos [5]).floatValue();
				String correoVisitante= (String) datos [6];
				
				Object [] resp = new Object [7];
				resp[0]="[Id Carnet: "+idCarnet;
				resp[1]="Cedula: "+cedula;
				resp[2]="Nombre: "+nombreVisitante;
				resp[3]="Telefono: "+telefonoVisitante;
				resp[4]="Nombre Contacto: "+nombreContactoVisitante;
				resp[5]="Telefono Contacto: "+telefonoContactoVisitante;
				resp[6]="Correo: "+correoVisitante+"]";
				
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC1AdminCentro(Timestamp fechaInicio, Timestamp fechaFin)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> visitantes=sqlVisita.RFC1AdminCentro(pm, fechaInicio, fechaFin);
			for(Object visitante: visitantes)
			{
				Object [] datos = (Object []) visitante;
				long idCarnet = ((BigDecimal) datos [0]).longValue ();
				long idEspacio= ((BigDecimal) datos [1]).longValue ();
				long cedula = ((BigDecimal) datos [2]).longValue ();
				String nombreVisitante= (String) datos [3];
				float telefonoVisitante= ((BigDecimal) datos [4]).floatValue();
				String nombreContactoVisitante= (String) datos [5];
				float telefonoContactoVisitante= ((BigDecimal) datos [6]).floatValue();
				String correoVisitante= (String) datos [7];
				
				Object [] resp = new Object [8];
				resp[0]="[Id Carnet: "+idCarnet;
				resp[1]="Id Espacio: "+idEspacio;
				resp[2]="Cedula: "+cedula;
				resp[3]="Nombre: "+nombreVisitante;
				resp[4]="Telefono: "+telefonoVisitante;
				resp[5]="Nombre Contacto: "+nombreContactoVisitante;
				resp[6]="Telefono Contacto: "+telefonoContactoVisitante;
				resp[7]="Correo: "+correoVisitante+"]";
				
				respuesta.add(resp);
			}
			
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC2(Timestamp fechaInicio, Timestamp fechaFin)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> visitantes=sqlVisita.RFC2(pm, fechaInicio, fechaFin);
			for(Object visitante: visitantes)
			{
				Object [] datos = (Object []) visitante;
				long idEstablecimiento= ((BigDecimal) datos [0]).longValue ();;
				String nombreEstablecimiento= (String) datos [1];
				String tipoEstablecimiento= (String) datos [2];
				int contadorVisitas= ((BigDecimal) datos [3]).intValue();
				
				Object [] resp = new Object [4];
				resp[0]="[Id Establecimiento: "+idEstablecimiento;
				resp[1]="Nombre establecimiento: "+nombreEstablecimiento;
				resp[2]="Tipo establecimiento: "+tipoEstablecimiento;
				resp[3]="Numero visitas: "+contadorVisitas;
				
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC7(Timestamp fecha1,Timestamp fecha2, String tipo)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> visitantes=sqlVisita.RFC7(pm, fecha1,fecha2,tipo);
			for(Object visitante: visitantes)
			{
				Object [] datos = (Object []) visitante;
				TIMESTAMP fechaXd= ((TIMESTAMP) datos [0]);
				int datoNumerico=((BigDecimal)datos[1]).intValue();
				
				Object [] resp = new Object [2];
				resp[0]="[Fecha: "+fechaXd+" ]";
				resp[1]=datoNumerico;
				
				
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public List<Object[]> RFC9(Timestamp fecha)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object []> respuesta = new LinkedList <Object []> ();
			List<Object> visitantes=sqlVisita.RFC9(pm, fecha);
			for(Object visitante: visitantes)
			{
				Object [] datos = (Object []) visitante;
				TIMESTAMP fechaXd= ((TIMESTAMP) datos [0]);
				long idCarnet= ((BigDecimal) datos [1]).longValue();
				String nombre= (String) datos [2];
				float telefono= ((BigDecimal) datos [3]).floatValue();
				String correo= (String) datos [4];
				String operacion= (String) datos [5];
				long idEspacio= ((BigDecimal) datos [6]).longValue();
				
				Object [] resp = new Object [7];
				resp[0]="[Fecha: "+fechaXd;
				resp[1]="Id carnet: "+idCarnet;
				resp[2]="Nombre visitante: "+nombre;
				resp[3]="Telefono visitante: "+telefono;
				resp[4]="Correo visitante: "+correo;
				resp[5]="Tipo operacion: "+operacion;
				resp[6]=idEspacio;
				
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}

	/* ****************************************************************
	 * 			Métodos para manejar la relación VISITANTE
	 *****************************************************************/
	public VISITANTE adicionarVisitante(float cedula, String nombre, float telefono,String nombre_contacto,float telefono_contacto, String codigo_qr, String correo, Timestamp horario_disponible, String tipo_visitante, long idEspacio, String estado) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlVisitante.adicionarVisitante(pm, cedula, nombre, telefono, nombre_contacto, telefono_contacto, codigo_qr, correo, horario_disponible, tipo_visitante, idEspacio, estado);
            tx.commit();

            log.trace ("Inserción de visitante: [" + cedula+ ", " 
            		+ nombre+ ", " + telefono+ ", " + nombre_contacto+ ", " + telefono_contacto+", " + codigo_qr+ ", " + correo+ ", " + horario_disponible+ ", " + tipo_visitante+", " + idEspacio+", "+estado+  "]. " 
            		+ tuplasInsertadas + " tuplas insertadas");

            return new VISITANTE (cedula, nombre, telefono, nombre_contacto, telefono_contacto, codigo_qr, correo, horario_disponible, tipo_visitante, idEspacio,estado);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarVisitantePorNombre(String nombre) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
	        Transaction tx=pm.currentTransaction();
	        try
	        {
	            tx.begin();
	            long resp = sqlVisitante.eliminarVisitantePorNombre(pm, nombre);
	            tx.commit();

	            return resp;
	        }
	        catch (Exception e)
	        {
//	        	e.printStackTrace();
	        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        	return -1;
	        }
	        finally
	        {
	            if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
	        }
	}
	public long eliminarVisitantePorCedula(float cedula) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
	        Transaction tx=pm.currentTransaction();
	        try
	        {
	            tx.begin();
	            long resp = sqlVisitante.eliminarVisitantePorCedula(pm, cedula);
	            tx.commit();

	            return resp;
	        }
	        catch (Exception e)
	        {
//	        	e.printStackTrace();
	        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        	return -1;
	        }
	        finally
	        {
	            if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
	        }
	}
	public List<VISITANTE> darVisitantePorNombre (String nombre) 
	{
		return sqlVisitante.darVisitantePorNombre(pmf.getPersistenceManager(), nombre);
	}
	public VISITANTE darVisitantePorCedula(float cedula) 
	{
		return sqlVisitante.darVisitantePorCEDULA(pmf.getPersistenceManager(), cedula);
	}
	public List<VISITANTE> darVisitantes ()
	{
		return sqlVisitante.darVisitantes(pmf.getPersistenceManager());
	}	
	@SuppressWarnings("deprecation")
	public List<Object[]> RFC5PorTipoVisitante(String tipo, Timestamp ts1, Timestamp ts2)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object[]> respuesta= new LinkedList<Object []> ();
			List<Object> resultado=sqlVisitante.RFC5PorTipoVisitante(pm, tipo, ts1, ts2);
			for(Object xd: resultado)
			{
				Object [] datos = (Object []) xd;
				long cedulaVisitante= ((BigDecimal) datos [0]).longValue();
				String nombreVisitante= (String) datos [1];
				TIMESTAMP fechaYHoraOp= (TIMESTAMP) datos [2];
				String TipoOperacion= (String) datos [3];
				TIMESTAMP horaFinOp= (TIMESTAMP) datos [4];
				long idEspacio= ((BigDecimal) datos [5]).longValue();
				//Convertir fecha inicial a Date de Java
				String xdd=fechaYHoraOp.stringValue();
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
      			DateFormat formatter = new SimpleDateFormat(FORMAT);
      			java.util.Date  w=formatter.parse(xdd);
      			//Convertir fecha inicial a Date de Java
				String xdd2=horaFinOp.stringValue();
      			java.util.Date  w2=formatter.parse(xdd2);
				//Obtener minutos, horas y segundos fecha inicio
      			int horas=w.getHours();
      			int minutos=w.getMinutes();
      			int segundos=w.getSeconds();
      			//Obtener minutos, horas y segundos fecha inicio				
      			int horas2=w2.getHours();
      			int minutos2=w2.getMinutes();
      			int segundos2=w2.getSeconds();
      			//Operaciones
      			int horasTotal=0;
      			int minutosTotal=0;
      			int segundosTotal=0;
      			if(segundos2<segundos)
      			{
      				segundosTotal=(segundos2+60)-segundos;
      				minutosTotal--;
      			}else{
      				segundosTotal=segundos2-segundos;
      			}
      			if(minutos2<minutos)
      			{
      				minutosTotal+=(minutos2+60)-minutos;
      				horasTotal--;
      			}else{
      				minutosTotal+=minutos2-minutos;
      			}
      			horasTotal+=horas2-horas;
      			//
      			
      			Object [] resp= new Object[9];
				resp[0]="[Cedula: "+cedulaVisitante;
				resp[1]="Nombre: "+nombreVisitante;
				resp[2]="Fecha inicio visita: "+fechaYHoraOp;
				resp[3]="Tipo operacoin: "+TipoOperacion;
				resp[4]="Fecha fin visita: "+horaFinOp;
				resp[5]="Id del espacio visitado: "+idEspacio;
				resp[6]=horasTotal;
				resp[7]=minutosTotal;
				resp[8]=segundosTotal;
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	@SuppressWarnings("deprecation")
	public List<Object[]> RFC6(long cedula, Timestamp ts1, Timestamp ts2)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			List<Object[]> respuesta= new LinkedList<Object []> ();
			List<Object> resultado=sqlVisitante.RFC6(pm, cedula, ts1, ts2);
			for(Object xd: resultado)
			{
				Object [] datos = (Object []) xd;
				long cedulaVisitante= ((BigDecimal) datos [0]).longValue();
				String nombreVisitante= (String) datos [1];
				TIMESTAMP fechaYHoraOp= (TIMESTAMP) datos [2];
				String TipoOperacion= (String) datos [3];
				TIMESTAMP horaFinOp= (TIMESTAMP) datos [4];
				long idEspacio= ((BigDecimal) datos [5]).longValue();
				String nombreEstabecimiento= (String) datos [6];
				//Convertir fecha inicial a Date de Java
				String xdd=fechaYHoraOp.stringValue();
				final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
      			DateFormat formatter = new SimpleDateFormat(FORMAT);
      			java.util.Date  w=formatter.parse(xdd);
      			//Convertir fecha inicial a Date de Java
				String xdd2=horaFinOp.stringValue();
      			java.util.Date  w2=formatter.parse(xdd2);
				//Obtener minutos, horas y segundos fecha inicio
      			int horas=w.getHours();
      			int minutos=w.getMinutes();
      			int segundos=w.getSeconds();
      			//Obtener minutos, horas y segundos fecha inicio				
      			int horas2=w2.getHours();
      			int minutos2=w2.getMinutes();
      			int segundos2=w2.getSeconds();
      			//Operaciones
      			int horasTotal=0;
      			int minutosTotal=0;
      			int segundosTotal=0;
      			if(segundos2<segundos)
      			{
      				segundosTotal=(segundos2+60)-segundos;
      				minutosTotal--;
      			}else{
      				segundosTotal=segundos2-segundos;
      			}
      			if(minutos2<minutos)
      			{
      				minutosTotal+=(minutos2+60)-minutos;
      				horasTotal--;
      			}else{
      				minutosTotal+=minutos2-minutos;
      			}
      			horasTotal+=horas2-horas;
      			//
      			
      			Object [] resp= new Object[10];
				resp[0]="[Cedula: "+cedulaVisitante;
				resp[1]="Nombre: "+nombreVisitante;
				resp[2]="Fecha inicio visita: "+fechaYHoraOp;
				resp[3]="Tipo operacoin: "+TipoOperacion;
				resp[4]="Fecha fin visita: "+horaFinOp;
				resp[5]="Id del espacio visitado: "+idEspacio;
				resp[6]="Nombre establecimiento: "+nombreEstabecimiento;
				resp[7]=horasTotal;
				resp[8]=minutosTotal;
				resp[9]=segundosTotal;
				respuesta.add(resp);
			}
			return respuesta;
		}catch(Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	public void RF8(String estado, long cedula)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			sqlVisitante.RF8RegistrarCambioDeEstado(pm, estado, cedula);
		}catch(Exception e)
        {
      	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }		
	}
	/* ****************************************************************
	 * 			Métodos para manejar la relación LECTOR
	 *****************************************************************/
	public LECTOR adicionarLector(long idEspacio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idLector = nextval ();
            long tuplasInsertadas = sqlLector.adicionarLector(pm, idLector, idEspacio);
            tx.commit();

            log.trace ("Inserción de lector: [" + idLector+ ", " 
            		+ idEspacio+"]. " 
            		+ tuplasInsertadas + " tuplas insertadas");

            return new LECTOR (idLector,idEspacio);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long eliminarLectorPorId(long idLector) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
	        Transaction tx=pm.currentTransaction();
	        try
	        {
	            tx.begin();
	            long resp = sqlLector.eliminarLector(pm, idLector);
	            tx.commit();

	            return resp;
	        }
	        catch (Exception e)
	        {
//	        	e.printStackTrace();
	        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
	        	return -1;
	        }
	        finally
	        {
	            if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
	        }
	}
	public LECTOR darLectorPorId(long id) 
	{
		return sqlLector.darLectorPorId(pmf.getPersistenceManager(), id);
	}
	public List<LECTOR> darLectores()
	{
		return sqlLector.darLECTORES(pmf.getPersistenceManager());
	}
	public long [] limpiarAforo ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long [] resp = sqlUtil.limpiarAforo (pm);
            tx.commit ();
            log.info ("Borrada la base de datos");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new long[] {-1, -1, -1, -1, -1, -1, -1};
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
 }
