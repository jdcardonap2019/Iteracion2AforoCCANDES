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

/**
 * Interfaz para los métodos get de GUSTAN.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Germán Bravo
 */
public interface VOLOCAL_COMERCIAL 
{
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * @return the iDESPACIO
	 */
	public long getIDESPACIO();
	/**
	 * @return the iD_LOCAL
	 */
	public long getID_LOCAL();
	/**
	 * @return the nOMBRE
	 */
	public String getNOMBRE();
	/**
	 * @return the nOMBRE_EMPRESA
	 */
	public String getNOMBRE_EMPRESA();
	/**
	 * @return the aREA
	 */
	public float getAREA();
	/**
	 * @return the tIPO_ESTABLECIMIENTO
	 */
	public String getTIPO_ESTABLECIMIENTO();
	@Override
	public String toString();
	
}
