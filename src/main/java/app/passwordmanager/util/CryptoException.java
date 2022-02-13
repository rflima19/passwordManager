package app.passwordmanager.util;

/**
 * Exceção de criptografia.
 * */
public class CryptoException extends RuntimeException {

	/**
	 * Controle de versão.
	 */
	private static final long serialVersionUID = 3867076221399526305L;

	public CryptoException() {
		super();
	}

	public CryptoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(Throwable cause) {
		super(cause);
	}
}
