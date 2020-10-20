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

import uniandes.isis2304.parranderos.negocio.LECTOR;
import uniandes.isis2304.parranderos.negocio.PARQUEADERO;

class SQLLECTOR 
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
	public SQLLECTOR (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	public long adicionarLector(PersistenceManager pm, long idLector, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaLECTOR()+ "(ID_LECTOR, IDESPACIO) values (?, ?)");
        q.setParameters(idLector, idEspacio);
        return (long) q.executeUnique();            
	}

	public long eliminarLector(PersistenceManager pm, long idLector)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaLECTOR() + " WHERE ID_LECTOR= ?");
        q.setParameters(idLector);
        return (long) q.executeUnique();            
	}
	public LECTOR darLectorPorId (PersistenceManager pm, long idLector) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaLECTOR() + " WHERE id = ?");
		q.setResultClass(LECTOR.class);
		q.setParameters(idLector);
		return (LECTOR) q.executeUnique();
	}
	public List<LECTOR> darLECTORES(PersistenceManager pm) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaLECTOR());
		q.setResultClass(LECTOR.class);
		return (List<LECTOR>) q.executeList();
	}
}
