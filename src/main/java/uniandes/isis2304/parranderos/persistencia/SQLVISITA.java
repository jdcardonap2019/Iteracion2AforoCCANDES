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

import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.VISITA;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto VISITAN de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLVISITA 
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
	public SQLVISITA (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	public long adicionarVisita (PersistenceManager pm, Timestamp fechaYHora_op , String tipo_op, Timestamp horafin_op,long IDCARNET, long IDLECTOR,long IDESPACIO) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVISITA() + "(FECHAYHORA_OP,TIPO_OP,HORAFIN_OP, IDLECTOR,IDCARNET,IDESPACIO) values (?, ?, ?, ?,?,?)");
        q.setParameters(fechaYHora_op ,  tipo_op,  horafin_op,IDCARNET,  IDLECTOR, IDESPACIO);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar TODAS LAS VISITAS de la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarVisitas (PersistenceManager pm) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITA ());
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN VISITAN de la base de datos de Parranderos, por sus identificadores
	 * @param pm - El manejador de persistencia
	 * @param idBebedor - El identificador del bebedor
	 * @param idBar - El identificador del bar
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarVisita (PersistenceManager pm, long idCarnet, long idLector, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITA () + " WHERE IDCARNET = ? AND IDLECTOR = ? AND IDESPACIO=?");
        q.setParameters(idCarnet, idLector,idEspacio);
        return (long) q.executeUnique();
	}


	
	public List<VISITA> darVisita (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITA ());
		q.setResultClass(VISITA.class);
		return (List<VISITA>) q.execute();
	}
		 	
}
