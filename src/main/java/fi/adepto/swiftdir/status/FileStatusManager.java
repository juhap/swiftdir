package fi.adepto.swiftdir.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Manages the status file. 
 * 
 * Status file is a simple way to integrate the directory downloader to external monitoring system.
 * If the download is ok, the file will exist but the length will be zero. Non zero lenght indicates
 * that something went wrong. More detailed (one line) message can be found from the file.
 *
 *
 */
public class FileStatusManager implements StatusManager {
	
	private File statusFile;
	
	public FileStatusManager(File statusFile) {
		this.statusFile = statusFile;
	}
	
	public void setSuccess() {
		writeToFile("");
	}
	
	public void setError(String errorMessage) {
		// Only pick first line from the given message. This keeps the status 
		// file simple and predicatable (it is likely to be processed with some other tool)
		if(errorMessage != null && errorMessage.length() > 0) {
			errorMessage = errorMessage.split("\\r?\\n")[0];
		}
		writeToFile("ERROR - " +  errorMessage);
	}
	
	private void writeToFile(String msg) {
		PrintStream ps;
		try {
			ps = new PrintStream(new FileOutputStream(statusFile), true, "UTF8");
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		if(msg != null) {
			ps.print(msg);
		}
		ps.close();
	}
}
