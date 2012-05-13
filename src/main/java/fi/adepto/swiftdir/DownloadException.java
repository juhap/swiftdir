package fi.adepto.swiftdir;


/**
 * Exception in HTTP download
 *
 */
public class DownloadException extends Exception {

	public DownloadException(Exception e) {
		super(e);
	}
	
	public DownloadException(String message) {
		super(message);
	}
}
