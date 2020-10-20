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

import uniandes.isis2304.parranderos.negocio.CARNET;

class SQLCARNET 
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
	public SQLCARNET (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	public long adicionarCarnet (PersistenceManager pm, long idCarnet, float cedula) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCARNET() + "(ID_CARNET, CEDULA) values (?, ?)");
        q.setParameters(idCarnet, cedula);
        return (long) q.executeUnique();
	}

	public long eliminarCarnetPorId(PersistenceManager pm, long idCarnet)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCARNET () + " WHERE ID_CARNET = ?");
        q.setParameters(idCarnet);
        return (long) q.executeUnique();            
	}
	
	public CARNET darCarnetPorId (PersistenceManager pm, long idCarnet) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCARNET() + " WHERE ID_CARNET = ?");
		q.setResultClass(CARNET.class);
		q.setParameters(idCarnet);
		return (CARNET) q.executeUnique();
	}
	public List<CARNET> darCarnets(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCARNET());
		q.setResultClass(CARNET.class);
		return (List<CARNET>) q.executeList();
	}
	public CARNET cambiarCedula(PersistenceManager pm,long idCarnet, float cedula) 
	{
		 Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCARNET()+ " SET NOMBRE = ? WHERE ID_CARNET= ?");
		 q.setParameters(idCarnet, cedula);
		 return (CARNET) q.executeUnique();
	}
}
