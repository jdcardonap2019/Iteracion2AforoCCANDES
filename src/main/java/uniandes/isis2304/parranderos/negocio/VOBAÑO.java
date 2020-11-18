package uniandes.isis2304.parranderos.negocio;

public interface VOBAÑO {
	/**
	 * @return the iDESPACIO
	 */
    public long getIDESPACIO();
    /**
	 * @return the iD_BAÑO
	 */
	public long getID_BAÑO();
	/**
	 * @return the nUMERO_SANITARIOS
	 */
	public int getNUMERO_SANITARIOS(); 
	@Override
	public String toString();
}
