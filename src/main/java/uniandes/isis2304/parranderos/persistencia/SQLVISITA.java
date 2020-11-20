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
	public List<Object> RFC7(PersistenceManager pm, Timestamp fecha1,Timestamp fecha2, String tipo)
	{
		String sql = "(SELECT A.flecho as flechin,B.SumaTotalAforoMaximo";
        sql += " FROM(SELECT flecho, jjjj, establi, idEsXd, SUM(numVisitas) as Sumavisitas";
        sql += " FROM(SELECT idEsXd, flecho, numVisitas, LOCAL_COMERCIAL.NOMBRE as jjjj, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as establi";
        sql += " FROM(SELECT IDESPACIO as idEsXd, FECHAYHORA_OP as flecho, COUNT(TIPO_OP) as NumVisitas";
        sql += " FROM "+pp.darTablaVISITA();
       	sql	+= " WHERE FECHAYHORA_OP BETWEEN ? AND ?";
      	sql	+= " GROUP BY IDESPACIO, FECHAYHORA_OP)";
       	sql	+= " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL() +" ON LOCAL_COMERCIAL.IDESPACIO=idEsXd AND LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO=?)";
       	sql	+= " GROUP BY flecho,  jjjj, establi, idEsXd";
       	sql	+= " ORDER BY SUM(numVisitas) DESC)A,";
       	sql	+= " (SELECT SUM(cd) as SumaTotalAforoMaximo";
       	sql += " FROM(SELECT ID_ESPACIO as idd, AFORO_TOTAL as cd";
        sql += " FROM "+pp.darTablaESPACIO()+")";
        sql += " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON LOCAL_COMERCIAL.IDESPACIO=idd AND local_comercial.tipo_establecimiento=?) B";
        sql += " WHERE ROWNUM<2)UNION";
        sql += " (SELECT A.flecho2 as flechin2, b.noventaporcientoaforomaximo as XD2";
       	sql	+= " FROM(SELECT flecho2, jjjj2, establi2, idEsXd2, SUM(numVisitas2) as Sumavisitas2";
      	sql	+= " FROM(SELECT idEsXd2, flecho2, numVisitas2, LOCAL_COMERCIAL.NOMBRE as jjjj2, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as establi2";
       	sql	+= " FROM(SELECT IDESPACIO as idEsXd2, FECHAYHORA_OP as flecho2, COUNT(TIPO_OP) as NumVisitas2";
       	sql	+= " FROM "+pp.darTablaVISITA();
       	sql	+= " WHERE FECHAYHORA_OP BETWEEN ? AND ?";
       	sql	+= " GROUP BY IDESPACIO, FECHAYHORA_OP)";
       	sql += " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON LOCAL_COMERCIAL.IDESPACIO=idEsXd2 AND LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO=?)";
        sql += " GROUP BY flecho2,  jjjj2, establi2, idEsXd2";
        sql += " ORDER BY SUM(numVisitas2) DESC)A,";
        sql += " (SELECT SUM(cd)*0.9 as NoventaPorcientoAforoMaximo";
        sql += " FROM(SELECT ID_ESPACIO as idd, AFORO_TOTAL as cd";
       	sql	+= " FROM "+pp.darTablaESPACIO()+")";
      	sql	+= " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON LOCAL_COMERCIAL.IDESPACIO=idd AND local_comercial.tipo_establecimiento=?) B";
       	sql	+= " WHERE A.SumaVisitas2>b.noventaporcientoaforomaximo AND ROWNUM<2)UNION";
       	sql	+= " (SELECT A.flecho3 as flechin3, b.diezporcientoaforomaximo as XD3";
       	sql	+= " FROM(SELECT flecho3, jjjj3, establi3, idEsXd3, SUM(numVisitas3) as Sumavisitas3";
       	sql	+= " FROM(SELECT idEsXd3, flecho3, numVisitas3, LOCAL_COMERCIAL.NOMBRE as jjjj3, LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO as establi3";
       	sql += " FROM(SELECT IDESPACIO as idEsXd3, FECHAYHORA_OP as flecho3, COUNT(TIPO_OP) as NumVisitas3";
        sql += " FROM "+pp.darTablaVISITA();
        sql += " WHERE FECHAYHORA_OP BETWEEN ? AND ?";
        sql += " GROUP BY IDESPACIO, FECHAYHORA_OP)";
        sql += " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON LOCAL_COMERCIAL.IDESPACIO=idEsXd3 AND LOCAL_COMERCIAL.TIPO_ESTABLECIMIENTO=?)";
       	sql	+= " GROUP BY flecho3,  jjjj3, establi3, idEsXd3";
      	sql	+= " ORDER BY SUM(numVisitas3) ASC)A,";
       	sql	+= " (SELECT SUM(cd)*0.1 as DiezPorcientoAforoMaximo";
       	sql	+= " FROM(SELECT ID_ESPACIO as idd, AFORO_TOTAL as cd";
       	sql	+= " FROM "+pp.darTablaESPACIO()+")";
       	sql	+= " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON LOCAL_COMERCIAL.IDESPACIO=idd AND local_comercial.tipo_establecimiento=?) B";
        sql += " WHERE A.SumaVisitas3<b.diezporcientoaforomaximo AND ROWNUM<2)";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(fecha1, fecha2, tipo, tipo,fecha1, fecha2, tipo, tipo,fecha1, fecha2, tipo, tipo );
		return (List)q.executeList();
	}
	public List<Object> RFC9(PersistenceManager pm, Timestamp fecha)
	{
		String sql = "SELECT fecha, usuarioCarnet, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.CORREO, op, espacio";
        sql += " FROM(SELECT visitaxs.IDESPACIO espacio, visitaxs.FECHAYHORA_OP fecha, visitaxs.IDCARNET as usuarioCarnet, visitaxs.tipo_op as op";
        sql += " FROM "+pp.darTablaVISITA()+" visitaxs, (SELECT * FROM "+pp.darTablaVISITA()+") visita2";
        sql += " WHERE visitaxs.IDESPACIO=visita2.IDESPACIO";
        sql += " AND visitaxs.fechayhora_op=visita2.fechayhora_op";
       	sql	+= " AND NOT visitaxs.idcarnet=visita2.idcarnet";
      	sql	+= " AND visitaxs.fechayhora_op BETWEEN ?-10 AND ?)";
       	sql	+= " INNER JOIN "+pp.darTablaCARNET()+" ON CARNET.ID_CARNET=usuarioCarnet";
       	sql	+= " INNER JOIN "+pp.darTablaVISITANTE()+" ON CARNET.CEDULA=VISITANTE.CEDULA";
       	sql	+= " GROUP BY fecha,usuarioCarnet,VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.CORREO, op, espacio";
       	sql	+= " ORDER BY espacio ASC";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(fecha, fecha);
		return q.executeList();
	}
		 	
}
