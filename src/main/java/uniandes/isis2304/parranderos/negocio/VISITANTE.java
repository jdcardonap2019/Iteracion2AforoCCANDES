package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;


public class VISITANTE implements VOVISITANTE{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * La cedula del visitante
	 */
	private float CEDULA;
	
	/**
	 * El nombre del visitante
	 */
	private String NOMBRE;
	
	/**
	 * Telefono del visitante
	 */
	private float TELEFONO;
	
	/**
	 * El nombre de un contacto del visitante
	 */
	private String NOMBRE_CONTACTO;
	/**
	 * Telefono de un contacto del visitante
	 */
	private float TELEFONO_CONTACTO;
	/**
	 * Posible codigo QR del visitante
	 */
	private String CODIGO_QR;
	/**
	 * El correo del visitante
	 */
	private String CORREO;
	/**
	 * Horario dispoible del usuario en el centro comercial
	 */
	private Timestamp HORARIO_DISPONIBLE;
	/**
	 * El tipo de visitante
	 */
	private String TIPO_VISITANTE;
	/**
	 * El id del espacio
	 */
	private long IDESPACIO;
	/**
	 * El Estado del espacio
	 */
	private String ESTADO;
	
	public VISITANTE() 
	{
		this.CEDULA= 0;
		this.NOMBRE = "";
		this.TELEFONO =0;
		this.NOMBRE_CONTACTO = "";
		this.TELEFONO_CONTACTO= 0;
		this.CODIGO_QR = "";
		this.CORREO = "";
		this.HORARIO_DISPONIBLE = new Timestamp(0);
		this.TIPO_VISITANTE = "";
		this.IDESPACIO = 0;
		this.ESTADO="";
	}

	public VISITANTE(float cedula, String nombre, float telefono, String nombreContacto,
			float telefonoContacto,String codigoQR, String correo,Timestamp horarioDisponible,
			String tipoVisitante, long idEspacio,String estado) 
	{
		this.CEDULA= cedula;
		this.NOMBRE = nombre;
		this.TELEFONO =telefono;
		this.NOMBRE_CONTACTO = nombreContacto;
		this.TELEFONO_CONTACTO= telefonoContacto;
		this.CODIGO_QR = codigoQR;
		this.CORREO = correo;
		this.HORARIO_DISPONIBLE = horarioDisponible;
		this.TIPO_VISITANTE = tipoVisitante;
		this.IDESPACIO = idEspacio;
		this.ESTADO = estado;
	}

	/**
	 * @return the cEDULA
	 */
	public float getCEDULA() {
		return CEDULA;
	}
	/**
	 * @param cEDULA the cEDULA to set
	 */
	public void setCEDULA(float cEDULA) {
		CEDULA = cEDULA;
	}
	/**
	 * @return the nOMBRE
	 */
	public String getNOMBRE() {
		return NOMBRE;
	}
	/**
	 * @param nOMBRE the nOMBRE to set
	 */
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}
	/**
	 * @return the tELEFONO
	 */
	public float getTELEFONO() {
		return TELEFONO;
	}
	/**
	 * @param tELEFONO the tELEFONO to set
	 */
	public void setTELEFONO(float tELEFONO) {
		TELEFONO = tELEFONO;
	}
	/**
	 * @return the nOMBRE_CONTACTO
	 */
	public String getNOMBRE_CONTACTO() {
		return NOMBRE_CONTACTO;
	}
	/**
	 * @param nOMBRE_CONTACTO the nOMBRE_CONTACTO to set
	 */
	public void setNOMBRE_CONTACTO(String nOMBRE_CONTACTO) {
		NOMBRE_CONTACTO = nOMBRE_CONTACTO;
	}
	/**
	 * @return the tELEFONO_CONTACTO
	 */
	public float getTELEFONO_CONTACTO() {
		return TELEFONO_CONTACTO;
	}
	/**
	 * @param tELEFONO_CONTACTO the tELEFONO_CONTACTO to set
	 */
	public void setTELEFONO_CONTACTO(float tELEFONO_CONTACTO) {
		TELEFONO_CONTACTO = tELEFONO_CONTACTO;
	}
	/**
	 * @return the cODIGO_QR
	 */
	public String getCODIGO_QR() {
		return CODIGO_QR;
	}
	/**
	 * @param cODIGO_QR the cODIGO_QR to set
	 */
	public void setCODIGO_QR(String cODIGO_QR) {
		CODIGO_QR = cODIGO_QR;
	}
	/**
	 * @return the cORREO
	 */
	public String getCORREO() {
		return CORREO;
	}
	/**
	 * @param cORREO the cORREO to set
	 */
	public void setCORREO(String cORREO) {
		CORREO = cORREO;
	}
	/**
	 * @return the hORARIO_DISPONIBLE
	 */
	public Timestamp getHORARIO_DISPONIBLE() {
		return HORARIO_DISPONIBLE;
	}
	/**
	 * @param hORARIO_DISPONIBLE the hORARIO_DISPONIBLE to set
	 */
	public void setHORARIO_DISPONIBLE(Timestamp hORARIO_DISPONIBLE) {
		HORARIO_DISPONIBLE = hORARIO_DISPONIBLE;
	}
	/**
	 * @return the tIPO_VISITANTE
	 */
	public String getTIPO_VISITANTE() {
		return TIPO_VISITANTE;
	}
	/**
	 * @param tIPO_VISITANTE the tIPO_VISITANTE to set
	 */
	public void setTIPO_VISITANTE(String tIPO_VISITANTE) {
		TIPO_VISITANTE = tIPO_VISITANTE;
	}
	/**
	 * @return the iDESPACIO
	 */
	public long getIDESPACIO() {
		return IDESPACIO;
	}
	/**
	 * @param iDESPACIO the iDESPACIO to set
	 */
	public void setIDESPACIO(long iDESPACIO) {
		IDESPACIO = iDESPACIO;
	}
	/**
	 * @return the ESTADO
	 */
	public String getESTADO()
	{
		return ESTADO;
	}
	/**
	 * 
	 * @param estado the ESTADO to set
	 */
	public void setESTADO(String estado) {
		ESTADO=estado; 
	}
}
