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

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.CENTRO_COMERCIAL;
import uniandes.isis2304.parranderos.negocio.LOCAL_COMERCIAL;
import uniandes.isis2304.parranderos.negocio.PARQUEADERO;


class SQLLOCAL_COMERCIAL 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAforo.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAforo pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLLOCAL_COMERCIAL (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	

	public long adicionarLocalComercial (PersistenceManager pm, long idEspacio,long id_local, String nombre, String nombre_empresa, float area, String tipo_establecimiento) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaLOCAL_COMERCIAL() + "(IDESPACIO, ID_LOCAL, NOMBRE, NOMBRE_EMPRESA,AREA,TIPO_ESTABLECIMIENTO) values (?,?,?,?,?,?)");
        q.setParameters(idEspacio,id_local, nombre,nombre_empresa,area,tipo_establecimiento);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar BEBIDAS de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombreBebida - El nombre de la bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarLocalComercialPorNombre (PersistenceManager pm, String nombreLocal)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaLOCAL_COMERCIAL() + " WHERE NOMBRE = ?");
        q.setParameters(nombreLocal);
        return (long) q.executeUnique();            
	}


	public long eliminarLocalComercialPorId (PersistenceManager pm, long idLocal)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaLOCAL_COMERCIAL() + " WHERE ID_LOCAL = ?");
        q.setParameters(idLocal);
        return (long) q.executeUnique();            
	}


	public LOCAL_COMERCIAL darLocalComercialPorId (PersistenceManager pm, long idLocal) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaLOCAL_COMERCIAL() + " WHERE ID_LOCAL = ?");
		q.setResultClass(LOCAL_COMERCIAL.class);
		q.setParameters(idLocal);
		return (LOCAL_COMERCIAL) q.executeUnique();
	}

	
	public List<LOCAL_COMERCIAL> darLocalComercialPorNombre (PersistenceManager pm, String nombreLocal) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaLOCAL_COMERCIAL() + " WHERE NOMBRE = ?");
		q.setResultClass(LOCAL_COMERCIAL.class);
		q.setParameters(nombreLocal);
		return (List<LOCAL_COMERCIAL>) q.executeList();
	}


	public List<LOCAL_COMERCIAL> darLocalesComerciales (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaLOCAL_COMERCIAL ());
		q.setResultClass(LOCAL_COMERCIAL.class);
		return (List<LOCAL_COMERCIAL>) q.executeList();
	}


}
