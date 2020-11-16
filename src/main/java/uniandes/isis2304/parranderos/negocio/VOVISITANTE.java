package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;

public interface VOVISITANTE {
	/**
	 * @return the cEDULA
	 */
	public float getCEDULA();
	/**
	 * @return the nOMBRE
	 */
	public String getNOMBRE();
	/**
	 * @return the tELEFONO
	 */
	public float getTELEFONO();
	/**
	 * @return the nOMBRE_CONTACTO
	 */
	public String getNOMBRE_CONTACTO();
	/**
	 * @return the tELEFONO_CONTACTO
	 */
	public float getTELEFONO_CONTACTO();
	/**
	 * @return the cODIGO_QR
	 */
	public String getCODIGO_QR();
	/**
	 * @return the cORREO
	 */
	public String getCORREO();
	/**
	 * @return the hORARIO_DISPONIBLE
	 */
	public Timestamp getHORARIO_DISPONIBLE();
	/**
	 * @return the tIPO_VISITANTE
	 */
	public String getTIPO_VISITANTE();
	/**
	 * @return the iDESPACIO
	 */
	public long getIDESPACIO();
	/**
	 * @return the Estado
	 */
	public String getESTADO();
	
	
}
