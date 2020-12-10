package uniandes.isis2304.parranderos.persistencia;

import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.VISITANTE;

public class SQLVISITANTE {
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
	public SQLVISITANTE (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	

	public long adicionarVisitante (PersistenceManager pm, float cedula, String nombre, float telefono,String nombre_contacto,float telefono_contacto, String codigo_qr, String correo, Timestamp horario_disponible, String tipo_visitante, long idEspacio, String estado) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVISITANTE() + "(CEDULA,NOMBRE,TELEFONO,NOMBRE_CONTACTO,TELEFONO_CONTACTO,CODIGO_QR,CORREO,HORARIO_DISPONIBLE,TIPO_VISITANTE,IDESPACIO, ESTADO) values (?, ?, ?, ?,?,?,?,?,?,?,?)");
        q.setParameters( cedula,  nombre,  telefono, nombre_contacto, telefono_contacto, codigo_qr, correo,  horario_disponible,  tipo_visitante,  idEspacio);
        return (long) q.executeUnique();
	}


	public long eliminarVisitantePorNombre (PersistenceManager pm, String nombre)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITANTE () + " WHERE NOMBRE = ?");
        q.setParameters(nombre);
        return (long) q.executeUnique();            
	}

	
	public long eliminarVisitantePorCedula (PersistenceManager pm, float cedula)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITANTE () + " WHERE CEDULA = ?");
        q.setParameters(cedula);
        return (long) q.executeUnique();            
	}


	public VISITANTE darVisitantePorCEDULA (PersistenceManager pm, float cedula) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITANTE () + " WHERE CEDULA = ?");
		q.setResultClass(VISITANTE.class);
		q.setParameters(cedula);
		return (VISITANTE) q.executeUnique();
	}


	public List<VISITANTE> darVisitantePorNombre (PersistenceManager pm, String nombreBebedor) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITANTE () + " WHERE NOMBRE = ?");
		q.setResultClass(VISITANTE.class);
		q.setParameters(nombreBebedor);
		return (List<VISITANTE>) q.executeList();
	}


	public List<VISITANTE> darVisitantes (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITANTE ());
		q.setResultClass(VISITANTE.class);
		return (List<VISITANTE>) q.executeList();
	}
	public List<Object> RFC5PorTipoVisitante(PersistenceManager pm,String tipo, Timestamp ts1, Timestamp ts2)
	{
		String sql = "SELECT CedulaVis, nombre, VISITA.FECHAYHORA_OP, VISITA.TIPO_OP, VISITA.HORAFIN_OP, visita.idespacio";
        sql += " FROM (SELECT CEDULA as CedulaVis, NOMBRE as nombre";
        sql += " FROM  "+pp.darTablaVISITANTE();
        sql += " WHERE TIPO_VISITANTE=?)";
        sql += " INNER JOIN "+pp.darTablaCARNET()+" ON carnet.cedula=CedulaVis";
        sql += " INNER JOIN "+pp.darTablaVISITA()+" ON CARNET.ID_CARNET=IDCARNET";
       	sql	+= " WHERE visita.fechayhora_op BETWEEN ? AND ?";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(tipo, ts1, ts2);
		return q.executeList();
	}
	public List<Object> RFC6(PersistenceManager pm,long cedula, Timestamp ts1, Timestamp ts2)
	{
		String sql = "SELECT cedulaXd, nombreOP, visita.fechayhora_op, visita.tipo_op, visita.horafin_op, visita.idespacio, LOCAL_COMERCIAL.NOMBRE";
        sql += " FROM (SELECT CEDULA as cedulaXd, NOMBRE as nombreOP";
        sql += " FROM  "+pp.darTablaVISITANTE();
        sql += " WHERE CEDULA=?)";
        sql += " INNER JOIN "+pp.darTablaCARNET()+" ON carnet.cedula=cedulaXd";
        sql += " INNER JOIN "+pp.darTablaVISITA()+" ON CARNET.ID_CARNET=VISITA.IDCARNET";
        sql += " INNER JOIN "+pp.darTablaLOCAL_COMERCIAL()+" ON VISITA.IDESPACIO=LOCAL_COMERCIAL.IDESPACIO";
       	sql	+= " WHERE visita.fechayhora_op BETWEEN ? AND ?";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(cedula, ts1, ts2);
		return q.executeList();
	}

	public void RF8RegistrarCambioDeEstado(PersistenceManager pm,String estado,long cedula)
	{
		String sql = "UPDATE ";
        sql += pp.darTablaVISITANTE();
        sql += " SET ESTADO=?";
        sql += " WHERE CEDULA=?";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(estado,cedula);
	}

	
	public List<Object> RFC10(PersistenceManager pm, Long id_local, Timestamp fechaInicio, Timestamp fechaFin, String ordenar)
	{ 
		String sql = "SELECT  VISITANTE.CEDULA, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.NOMBRE_CONTACTO, VISITANTE.TELEFONO_CONTACTO, VISITANTE.CORREO";
		sql +=" FROM(SELECT DISTINCT(IDCARNET)";
		sql+=" FROM "+pp.darTablaVISITA();
		sql+=" WHERE FECHAYHORA_OP BETWEEN ? AND ?";
		sql+=" AND TIPO_OP='Entrada'";
		sql+=" AND IDESPACIO=?)";
		sql+=" INNER JOIN "+pp.darTablaCARNET()+" ON CARNET.ID_CARNET=IDCARNET";
		sql+=" INNER JOIN "+pp.darTablaVISITANTE()+" ON VISITANTE.CEDULA=CARNET.CEDULA";		
		if(ordenar.contains("CEDULA") || ordenar.contains("NOMBRE") ||
				ordenar.contains("TELEFONO") || ordenar.contains("NOMBRE_CONTACTO") || 
				ordenar.contains("TELEFONO_CONTACTO") || ordenar.contains("CORREO")) 
		{
			sql+=" ORDER BY "+ ordenar;
		}	
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicio,fechaFin,id_local);
		return q.executeList();
	}

	public List<Object> RFC11(PersistenceManager pm, Long id_local, Timestamp fechaInicio, Timestamp fechaFin, String ordenar)
	{ 
		String sql = "SELECT  VISITANTE.CEDULA, VISITANTE.NOMBRE, VISITANTE.TELEFONO, VISITANTE.NOMBRE_CONTACTO, VISITANTE.TELEFONO_CONTACTO, VISITANTE.CORREO";
		sql +=" FROM(SELECT DISTINCT(IDCARNET)";
		sql+=" FROM "+pp.darTablaVISITA();
		sql+=" WHERE FECHAYHORA_OP BETWEEN ? AND ?";
		sql+=" AND TIPO_OP='Entrada'";
		sql+=" AND IDESPACIO=?)";
		sql+=" RIGHT JOIN "+pp.darTablaCARNET()+" ON CARNET.ID_CARNET=IDCARNET";
		sql+=" INNER JOIN "+pp.darTablaVISITANTE()+" ON VISITANTE.CEDULA=CARNET.CEDULA";
		sql+=" WHERE IDCARNET IS NULL";
		if(ordenar.contains("CEDULA") || ordenar.contains("NOMBRE") ||
				ordenar.contains("TELEFONO") || ordenar.contains("NOMBRE_CONTACTO") || 
				ordenar.contains("TELEFONO_CONTACTO") || ordenar.contains("CORREO")) 
		{
			sql+=" ORDER BY "+ ordenar;
		}		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicio,fechaFin,id_local,fechaInicio,fechaFin,id_local);
		return q.executeList();
	}

}
