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

import uniandes.isis2304.parranderos.negocio.LOCAL_COMERCIAL;


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
	public List<Object> RFC5PorTipoLocal(PersistenceManager pm,String tipo, Timestamp ts1, Timestamp ts2)
	{
		String sql = "SELECT IdEspacioXd, nombre, COUNT(VISITA.TIPO_OP) as NumeroVisitas";
        sql += " FROM (SELECT IDESPACIO as IdEspacioXd, NOMBRE as nombre";
        sql += " FROM  "+pp.darTablaLOCAL_COMERCIAL();
        sql += " WHERE TIPO_ESTABLECIMIENTO=?)";
        sql += " INNER JOIN "+pp.darTablaVISITA()+" ON IdEspacioXd=visita.idespacio";
       	sql	+= " WHERE visita.fechayhora_op BETWEEN ? AND ?";
       	sql	+= " GROUP BY IdEspacioXd, nombre";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(tipo, ts1, ts2);
		return q.executeList();
	}
	public List<Object> RFC8(PersistenceManager pm,String nombreRestaurante	)
	{
		String sql = "SELECT VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.CORREO, idCarnetUsuario, numVisitasEseMes, mes";
        sql += " FROM(SELECT aidi, name, mes, idCarnetUsuario, numVisitasEseMes";
        sql += " FROM(SELECT aidi, name, EXTRACT(MONTH FROM CAST(FECHAYHORA_OP as DATE)) as mes, VISITA.IDCARNET as idCarnetUsuario, COUNT(VISITA.FECHAYHORA_OP) as numVisitasEseMes";
        sql += " FROM(SELECT IDESPACIO as aidi, NOMBRE as name";
        sql += " FROM  "+pp.darTablaLOCAL_COMERCIAL();
        sql += " WHERE NOMBRE=?)";
        sql += " INNER JOIN "+pp.darTablaVISITA()+" ON VISITA.IDESPACIO=aidi";
       	sql	+= " GROUP BY VISITA.IDCARNET, EXTRACT(MONTH FROM CAST(FECHAYHORA_OP as DATE)), aidi, name)";
       	sql	+= " WHERE numVisitasEseMes>=3)";
       	sql	+= " INNER JOIN CARNET ON CARNET.ID_CARNET=idCarnetUsuario";
       	sql	+= " INNER JOIN VISITANTE ON CARNET.CEDULA=VISITANTE.CEDULA AND NOT VISITANTE.TIPO_VISITANTE='Mantenimiento' AND NOT VISITANTE.TIPO_VISITANTE='Domiciliarios'";
       	sql	+= " GROUP BY VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.CORREO, idCarnetUsuario, numVisitasEseMes, mes";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(nombreRestaurante);
		return q.executeList();
	}

}
