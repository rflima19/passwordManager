package app.passwordmanager.dao;

/**
 * Exceção de DAO
 */
public class DAOException extends RuntimeException {

	/**
	 * Controle de versão.
	 */
	private static final long serialVersionUID = -4484801904671057277L;

	public DAOException() {
		super();
	}

	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

}
