package fi.adepto.swiftdir.status;

/**
 * StatusManager that does nothing. Used when no status file is not in use.
 *
 */
public class NoOpStatusManager implements StatusManager {
	@Override
	public void setSuccess() {
	}

	@Override
	public void setError(String errorMessage) {
	}
}
