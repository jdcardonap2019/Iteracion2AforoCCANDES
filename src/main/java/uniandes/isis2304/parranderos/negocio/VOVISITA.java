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

/**
 * Interfaz para los métodos get de VISITAN.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Germán Bravo
 */
public interface VOVISITA 
{
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * @return the fECHAYHORA_OP
	 */
	public Timestamp getFECHAYHORA_OP();
	/**
	 * @return the tIPO_OP
	 */
	public String getTIPO_OP();
	/**
	 * @return the hORAFIN_OP
	 */
	public Timestamp getHORAFIN_OP();
	/**
	 * @return the iDLECTOR
	 */
	public long getIDLECTOR();
	/**
	 * @return the iDCARNET
	 */
	public long getIDCARNET();
	/**
	 * @return the iDESPACIO
	 */
	public long getIDESPACIO();
	@Override
	public String toString();
}
