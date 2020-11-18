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

package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;




public class ESPACIO implements VOESPACIO
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del espacio
	 */
	private long ID_ESPACIO;

	/**
	 * El horario de apertura de los empleados para el espacio
	 */
	private Timestamp HORARIO_APERTURA_EMPLEADOS;
	/**
	 * El horario de apertura de los clientes para el espacio
	 */
	private Timestamp HORARIO_APERTURA_CLIENTES;
	/**
	 * El horario de cierre de los clientes y empleados para el espacio
	 */
	private Timestamp HORARIO_CIERRE_CLIENTES;
	/**
	 * El afoto actual del espacio
	 */
	private int AFORO_ACTUAL;
	/**
	 * El aforo total del espacio
	 */
	private int AFORO_TOTAL;
	/**
	 * El aforo total del espacio
	 */
	private String ESTADO;


	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public ESPACIO() 
	{
		this.AFORO_ACTUAL = 0;
		this.AFORO_TOTAL = 0;
		this.HORARIO_APERTURA_CLIENTES = new Timestamp(0);
		this.HORARIO_APERTURA_EMPLEADOS = new Timestamp(0);
		this.HORARIO_CIERRE_CLIENTES = new Timestamp(0);
		this.ID_ESPACIO = 0;
		this.ESTADO ="";

	}

	public ESPACIO(long idEspacio, Timestamp horarioAperturaEmpleados, Timestamp horarioAperturaClientes,
			Timestamp horarioCierreClientes, int aforoActual, int aforoTotal,String estado) 
	{		
		this.ID_ESPACIO = idEspacio;
		this.HORARIO_APERTURA_EMPLEADOS = horarioAperturaEmpleados;
		this.HORARIO_APERTURA_CLIENTES = horarioAperturaClientes;
		this.HORARIO_CIERRE_CLIENTES = horarioCierreClientes;
		this.AFORO_ACTUAL = aforoActual;
		this.AFORO_TOTAL = aforoTotal;
		this.ESTADO = estado;
	}

	/**
	 * @return the iD_ESPACIO
	 */
	public long getID_ESPACIO() {
		return ID_ESPACIO;
	}

	/**
	 * @param iD_ESPACIO the iD_ESPACIO to set
	 */
	public void setID_ESPACIO(long iD_ESPACIO) {
		ID_ESPACIO = iD_ESPACIO;
	}
   /**
    * @return the Estado
    */
	public String getESTADO() {
		return ESTADO;
	}
	/**
	 * 
	 * @param estado the ESTADO to set
	 */
	public void setESTADO(String estado)
	{
		ESTADO= estado;
	}
	/**
	 * @return the hORARIO_APERTURA_EMPLEADOS
	 */
	public Timestamp getHORARIO_APERTURA_EMPLEADOS() {
		return HORARIO_APERTURA_EMPLEADOS;
	}

	/**
	 * @param hORARIO_APERTURA_EMPLEADOS the hORARIO_APERTURA_EMPLEADOS to set
	 */
	public void setHORARIO_APERTURA_EMPLEADOS(Timestamp hORARIO_APERTURA_EMPLEADOS) {
		HORARIO_APERTURA_EMPLEADOS = hORARIO_APERTURA_EMPLEADOS;
	}

	/**
	 * @return the hORARIO_APERTURA_CLIENTES
	 */
	public Timestamp getHORARIO_APERTURA_CLIENTES() {
		return HORARIO_APERTURA_CLIENTES;
	}

	/**
	 * @param hORARIO_APERTURA_CLIENTES the hORARIO_APERTURA_CLIENTES to set
	 */
	public void setHORARIO_APERTURA_CLIENTES(Timestamp hORARIO_APERTURA_CLIENTES) {
		HORARIO_APERTURA_CLIENTES = hORARIO_APERTURA_CLIENTES;
	}

	/**
	 * @return the hORARIO_CIERRE_CLIENTES
	 */
	public Timestamp getHORARIO_CIERRE_CLIENTES() {
		return HORARIO_CIERRE_CLIENTES;
	}

	/**
	 * @param hORARIO_CIERRE_CLIENTES the hORARIO_CIERRE_CLIENTES to set
	 */
	public void setHORARIO_CIERRE_CLIENTES(Timestamp hORARIO_CIERRE_CLIENTES) {
		HORARIO_CIERRE_CLIENTES = hORARIO_CIERRE_CLIENTES;
	}

	/**
	 * @return the aFORO_ACTUAL
	 */
	public int getAFORO_ACTUAL() {
		return AFORO_ACTUAL;
	}

	/**
	 * @param aFORO_ACTUAL the aFORO_ACTUAL to set
	 */
	public void setAFORO_ACTUAL(int aFORO_ACTUAL) {
		AFORO_ACTUAL = aFORO_ACTUAL;
	}

	/**
	 * @return the aFORO_TOTAL
	 */
	public int getAFORO_TOTAL() {
		return AFORO_TOTAL;
	}

	/**
	 * @param aFORO_TOTAL the aFORO_TOTAL to set
	 */
	public void setAFORO_TOTAL(int aFORO_TOTAL) {
		AFORO_TOTAL = aFORO_TOTAL;
	}
	@Override
	public String toString() 
	{
		String resultado= "Espacio [id=" + ID_ESPACIO + ", horario apertura empleados=" + 
	HORARIO_APERTURA_EMPLEADOS+ ", horario apertura clientes=" + HORARIO_APERTURA_CLIENTES+ 
	", horario cierre clientes=" + HORARIO_CIERRE_CLIENTES+ ", aforo actual="+ AFORO_ACTUAL+
	", aforo total="+ AFORO_TOTAL+", estado="+ ESTADO+ "]";
		return resultado;
	}
}
