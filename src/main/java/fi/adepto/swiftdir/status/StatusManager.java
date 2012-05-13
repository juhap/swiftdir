package fi.adepto.swiftdir.status;

/**
 * Provides information to other systems (for example monitoring service) about the
 * status of last download operation (was it successful or not)
 *
 * Only most recent status is maintained. 
 *
 *
 */
public interface StatusManager {
	/**
	 * Set status to success
	 */
	void setSuccess();
	/**
	 * Set status to error
	 * @param errorMessage detailed one line error message
	 */
	void setError(String errorMessage);
}
