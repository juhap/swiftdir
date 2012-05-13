package fi.adepto.swiftdir.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fi.adepto.swiftdir.DownloadJob.DownloadResult;

/**
 * Extract contents of the downloaded file to target directory.
 * 
 * Only zipped (dos-format) files are supported.
 *
 */
public class UnzipOutputProcessor implements ResultProcessor {

	private final Log log = LogFactory.getLog(getClass());
	private final File targetDir;
	
	public UnzipOutputProcessor(File targetDir) {		
		this.targetDir = targetDir;
	}
	
	@Override
	public void process(DownloadResult result) throws IOException {
		log.info(String.format("Extracting compressed file %s to folder %s", result.getFilename(), targetDir.getAbsolutePath()));
		ZipInputStream zis = new ZipInputStream(result.getInputStream());
		ZipEntry entry = null;
		while((entry = zis.getNextEntry()) != null) {
			log.info(String.format("Extracting file %s", entry.getName()));
			OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(targetDir, entry.getName())));
			for(int c=zis.read(); c != -1; c=zis.read()) {
				os.write(c);
			}
			zis.closeEntry();
			os.close();
		}	
		zis.close();
		log.info("Files extracted");
	}

}
