import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Request implements Runnable {

	private String proxy = null;
	private int port;
	private URLConnection conn = null;
	private URL url = null;
	private String surl = null;

	public Request(String strURL, String strProxy, int iProxyPort)
			throws Exception {
		proxy = strProxy;
		port = iProxyPort;
		url = new URL(strURL);
		surl = strURL;
	}

	public void run() {
		try {

			URL urlProxy = new URL(url.getProtocol(), proxy, port, surl);
			url = urlProxy;

			conn = url.openConnection();

			HttpURLConnection http = (HttpURLConnection) conn;
			http.setConnectTimeout(15000);
			http.setReadTimeout(15000);

			http.connect();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Main.hits++;
				System.out.println("Using Proxy: " + proxy + ":" + port
						+ "\nHTTP Status: " + http.getResponseMessage() + " ("
						+ http.getResponseCode() + ")\n" + "Current Hits: "
						+ Main.hits + "\n");

			}

			http.disconnect();
		} catch (Exception ignore) {
			// If connection timed out
		} finally {
			synchronized (Main.sync) {
				Main.currentThreads--;
				Main.sync.notify();
			}
		}
	}

}
