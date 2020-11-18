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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
	public long eliminarVisita (PersistenceManager pm, long idCarnet, long idLector, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITA () + " WHERE IDCARNET = ? AND IDLECTOR = ? AND IDESPACIO=?");
        q.setParameters(idCarnet, idLector,idEspacio);
        return (long) q.executeUnique();
	}
	public long eliminarVisitasPorIdLector (PersistenceManager pm, long idLector) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITA () + " WHERE IDLECTOR = ?");
        q.setParameters(idLector);
        return (long) q.executeUnique();
	}
	public long eliminarVisitasPorIdEspacio (PersistenceManager pm, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITA () + " WHERE IDESPACIO = ?");
        q.setParameters(idEspacio);
        return (long) q.executeUnique();
	}

	
	public List<VISITA> darVisitas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITA ());
		q.setResultClass(VISITA.class);
		return (List<VISITA>) q.execute();
	}
	public List<Object> RFC1AdminEstablecimiento (PersistenceManager pm,long idEspacio, Timestamp fechaInicio, Timestamp fechaFin)
	{
		
		String sql = "SELECT Carnet, VISITANTE.CEDULA, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.NOMBRE_CONTACTO, VISITANTE.TELEFONO_CONTACTO, VISITANTE.CORREO";
        sql += " FROM(SELECT IDCARNET as Carnet ";
        sql += " FROM  "+pp.darTablaVISITA();
        sql += " WHERE idespacio=? AND FECHAYHORA_OP BETWEEN ? AND ?) ";
        sql += " INNER JOIN "+pp.darTablaCARNET()+" ON CARNET.ID_CARNET=Carnet ";
       	sql	+= " INNER JOIN "+pp.darTablaVISITANTE()+" ON CARNET.CEDULA=VISITANTE.CEDULA";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(idEspacio, fechaInicio, fechaFin);
		ResultSet x=null;
		return q.executeList();
	}
	public List<Object> RFC1AdminCentro (PersistenceManager pm, Timestamp fechaInicio, Timestamp fechaFin)
	{
		String sql = "SELECT Carnet, xs as IdEspacio, VISITANTE.CEDULA, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.NOMBRE_CONTACTO, VISITANTE.TELEFONO_CONTACTO, VISITANTE.CORREO";
        sql += " FROM (SELECT IDCARNET as Carnet, IDESPACIO as xs ";
        sql += " FROM  "+pp.darTablaVISITA();
        sql += " WHERE FECHAYHORA_OP BETWEEN ? AND ?) ";
        sql += " INNER JOIN "+pp.darTablaCARNET()+" ON CARNET.ID_CARNET=Carnet ";
       	sql	+= " INNER JOIN "+pp.darTablaVISITANTE()+" ON CARNET.CEDULA=VISITANTE.CEDULA";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicio, fechaFin);
		return q.executeList();
	}
	public List<Object> RFC2 (PersistenceManager pm, Timestamp fechaInicio, Timestamp fechaFin)
	{
		String sql = "SELECT LOCAL_COMERCIAL.idespacio as IdEstablecimiento,local_comercial.nombre, local_comercial.tipo_establecimiento as Tipo,contadorVisitas";
        sql += " FROM(SELECT IDESPACIO as IDESPACIOXD, COUNT(DISTINCT IDCARNET)as contadorVisitas ";
        sql += " FROM  "+pp.darTablaVISITA();
        sql += " WHERE FECHAYHORA_OP BETWEEN ? AND ? ";
        sql += " GROUP BY IDESPACIO ";
       	sql	+= " HAVING COUNT(DISTINCT IDCARNET)>0) ";
       	sql	+= " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON IDESPACIOXD=LOCAL_COMERCIAL.idespacio ";
       	sql	+= " WHERE ROWNUM<=20 ";
       	sql	+= " ORDER BY contadorVisitas DESC";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicio, fechaFin);
		return q.executeList();
	}
		 	
}
