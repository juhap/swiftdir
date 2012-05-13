package fi.adepto.swiftdir;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DownloadJob {

	private static final Log log = LogFactory.getLog(Downloader.class);
	private final UsernamePasswordCredentials credentials;
	private final String baseUrl;
	private final Collection<NameValuePair> params;
	private final String authScopeHost;

	public DownloadJob(Config config) {
		this.credentials = new UsernamePasswordCredentials(config.getUsername(), config.getPassword());
		this.baseUrl = config.getBaseUrl();
		this.params = config.getParams();
		this.authScopeHost = config.getAuthScopeHost();
	}
	
	private void paramsToString(Collection<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for(NameValuePair pair : params) {
			if(sb.length() > 0) sb.append(", ");
			sb.append(String.format("%s=%S", pair.getName(), pair.getValue()));
		}
	}

	/**
	 * Perform download
	 * 
	 * @param os
	 *            Output stream to write the results to (could be for
	 *            example file)
	 * @throws DownloadException 
	 */
	public DownloadResult execute() throws DownloadException {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(baseUrl);
		
		// Provide custom retry handler if required
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		// Set security credentials
		client.getState().setCredentials(new AuthScope(authScopeHost, 443), credentials);

		// Set query string parameters
		method.setQueryString(params.toArray(new NameValuePair[] {}));

		try {
			log.info(String.format("Downloading as user %s from url %s", credentials.getUserName(), method.getQueryString()));
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Request failed: "
						+ method.getStatusLine() + "\n"
						+ method.getResponseBodyAsString());

				throw new DownloadException(String.format(
						"HTTP error %d occurred. %s. %s.", 
						statusCode, 
						method.getStatusLine(), 
						method.getResponseBodyAsString()));
			} else {
				log.info("Request successfull");
				// Get the filename from the response body.
				InputStream is = method.getResponseBodyAsStream();
				Header[] contdisp = method.getResponseHeaders("Content-Disposition");

				String filename = null;
				StringTokenizer sttwo = new StringTokenizer(contdisp[0].getValue(), "=");
				while (sttwo.hasMoreTokens())
					filename = sttwo.nextToken();

				return new DownloadResult(filename, is);					
			}
		} catch (HttpException e) {
			throw new DownloadException(e);
		} catch(IOException e) {
			throw new DownloadException(e);
		} finally {
			//method.releaseConnection();
		}
	}
	
	public class DownloadResult {
		private final String filename;
		private final InputStream inputStream;

		private DownloadResult(String filename, InputStream is) {
			this.filename = filename;
			this.inputStream = is;
		}
				
		/** 
		 * @return Name of the file
		 */
		public String getFilename() {
			return filename;
		}
		
		/**
		 * @return InputStream containing the downloaded file
		 * 
		 */
		public InputStream getInputStream() {
			return inputStream;
		}
		
		/**
		 * Close down the result (inputStream)
		 */
		public void close() {
			try {
				inputStream.close();
			} catch(IOException e) {
				// ignored on purpose
			}
		}
	}
}