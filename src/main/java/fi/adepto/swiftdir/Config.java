package fi.adepto.swiftdir;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


import org.apache.commons.httpclient.NameValuePair;

import com.beust.jcommander.Parameter;

import fi.adepto.swiftdir.processor.ResultProcessor;
import fi.adepto.swiftdir.processor.SaveToDiskOutputProcessor;
import fi.adepto.swiftdir.processor.UnzipOutputProcessor;
import fi.adepto.swiftdir.status.FileStatusManager;
import fi.adepto.swiftdir.status.NoOpStatusManager;
import fi.adepto.swiftdir.status.StatusManager;

public class Config {
	
	private static final String baseUrl = "https://www2.swift.com/bicdownload/bicdownloader";
	private static final String authScopeHost = "www2.swift.com";
	
	@Parameter(names="-content", description="full or delta", required=true)
	private String content;
	
	@Parameter(names="-username", description="username for SWIFT website", required=true)
	private String username;
	
	@Parameter(names="-password", description="password for SWIFT website", required=true)
	private String password;
	
	@Parameter(names="-productline", description="product category (eg. swbankfile, bicdir, bicplusiban, separouting, ...)", required=true)
	private String productline;
	
	@Parameter(names="-product", description="product under product category (bankfile, bicplusiban, ...)", required=true)
	private String product;
	
	@Parameter(names="-format", description="file content format (txt, dat, ebcdic, dos)", required=true)
	private String format;
	
	@Parameter(names="-platform", description="Windows ZIP or unix compressed file (win, unix)", required=true)
	private String platform;
	
	@Parameter(names="-year", description="If omitted, defaults to latest available file of selected type")
	private String year;
	
	@Parameter(names="-month", description="If omitted, defaults to month of latest available file of selected type")
	private String month;
	
	@Parameter(names="-targetDir", description="Target directory for the downloaded file")
	private String targetDir;
	
	@Parameter(names="-unzip", description="Uncompress zip file to targetDir (.tar.Z is not supported)")
	private boolean unzip;
	
	@Parameter(names="-statusFile", description="Path to statusfile (will be empty if download was succesfull, otherwise contains error message on one line)")
	private String statusFile;
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}	
	
	public String getAuthScopeHost() {
		return authScopeHost;
	}
		
	public StatusManager getStatusManager() {
		if(statusFile != null) {
			return new FileStatusManager(new File(statusFile));
		} else {
			return new NoOpStatusManager();
		}		
	}
	
	public ResultProcessor getOutputProcessor() {
		if(unzip) {
			return new UnzipOutputProcessor(new File(targetDir));
		} else {
			return new SaveToDiskOutputProcessor(new File(targetDir));
		}
	}
	
	public Collection<NameValuePair> getParams() {
		Collection<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("action", "getfile"));
		params.add(	new NameValuePair("content", content));
		params.add(new NameValuePair("productline", productline));
		params.add(new NameValuePair("product", product));
		params.add(new NameValuePair("format", format));
		params.add(new NameValuePair("platform", platform));
		if(year != null) {
			params.add(new NameValuePair("year", year));	
		}
		if(month != null) {
			params.add(new NameValuePair("month", month));	
		}		
		
		return params;
	}


	


}
