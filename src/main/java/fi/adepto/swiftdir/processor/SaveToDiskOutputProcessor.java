package fi.adepto.swiftdir.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fi.adepto.swiftdir.DownloadJob.DownloadResult;


/**
 * Download file to disk, to given target directory 
 *
 */
public class SaveToDiskOutputProcessor implements ResultProcessor {

	private final Log log = LogFactory.getLog(getClass());
	private File targetDir;
	
	public SaveToDiskOutputProcessor(File targetDir) {
		this.targetDir = targetDir;
	}
	
	@Override
	public void process(DownloadResult result) throws IOException {
		log.info(String.format("Saving file %s to folder %s", result.getFilename(), targetDir.getAbsolutePath()));
		
		OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(targetDir, result.getFilename())));
		copy(result.getInputStream(), os);
		os.close();
		
		log.info("File saved succesfully");
	}
	
	private void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[4096];

		int count;
		while ((count = is.read(buffer)) > 0) {
			os.write(buffer, 0, count);
		}
		os.flush();
	}
}
