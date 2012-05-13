package fi.adepto.swiftdir.processor;

import java.io.IOException;

import fi.adepto.swiftdir.DownloadJob.DownloadResult;


/**
 * Processing for the downloaded file. 
 * 
 * The file can be extracted (zip) or just saved to disk.
 *
 */
public interface ResultProcessor {
	/**
	 * Process the downloaded file
	 * 
	 * @param result
	 * @throws IOException
	 */
	void process(DownloadResult result) throws IOException;
}
