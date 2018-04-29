package br.com.coutinho.b2w.swapi.exception;

/**
 * Customized exception for validation errors
 * 
 * @author Rafael Coutinho
 *
 */
public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -414773007968533147L;

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
