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


public interface VOESPACIO 
{
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * @return the iD_ESPACIO
	 */
	public long getID_ESPACIO();
	/**
	 * @return the hORARIO_APERTURA_EMPLEADOS
	 */
	public Timestamp getHORARIO_APERTURA_EMPLEADOS();
	/**
	 * @return the hORARIO_APERTURA_CLIENTES
	 */
	public Timestamp getHORARIO_APERTURA_CLIENTES();

	/**
	 * @return the hORARIO_CIERRE_CLIENTES
	 */
	public Timestamp getHORARIO_CIERRE_CLIENTES();
	/**
	 * @return the aFORO_ACTUAL
	 */
	public int getAFORO_ACTUAL();
	/**
	 * @return the aFORO_TOTAL
	 */
	public int getAFORO_TOTAL();
	/**
	 * @return the Estado 
	 */
	public String getESTADO();
	
	@Override
	public String toString();

}
