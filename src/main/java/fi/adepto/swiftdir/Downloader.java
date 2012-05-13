package fi.adepto.swiftdir;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import fi.adepto.swiftdir.DownloadJob.DownloadResult;
import fi.adepto.swiftdir.status.StatusManager;


public class Downloader {
	
	private static final Log log = LogFactory.getLog(Downloader.class);
	
	/**
	 * Executes the download operation specified via the command line.
	 * 
	 * Main will exit with code:
	 * 
	 * 		0 	when everything is ok
	 * 		1	if there is problem with command line
	 * 		2 	if something else goes wrong
	 * 
	 */
	public static void main(String[] args) {

		Config config = new Config();		
		try {
			new JCommander(config, args);
		} catch(ParameterException e) {
			System.err.println("Error parsing commandline, " + e.getMessage());
			new JCommander(config).usage();
			
			System.exit(1);
		}		

		StatusManager statusManager = config.getStatusManager();
		try {
			download(config);
			statusManager.setSuccess();
			
			System.exit(0);
						
		} catch(Exception e) {	
			statusManager.setError(e.getMessage());			
			log.error("Exception occured.", e);
			System.exit(2);
		} 		
	}
	
	/**
	 * Perform download task specified in the configuration 
	 * 
	 * @param config
	 * @throws IOException
	 * @throws DownloadException
	 */
	private static void download(Config config) throws IOException, DownloadException {
		DownloadJob job = new DownloadJob(config);		
		DownloadResult result = job.execute();	
		config.getOutputProcessor().process(result);
	}
}