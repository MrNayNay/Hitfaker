import java.io.BufferedReader;
import java.io.FileReader;

/*
 * Par1: URL 
 * Par2: Proxy List
 * Par3: Maximum of Threads
 */

public class Main {

	static private String surl;
	static private String proxy = null;
	static private int port;
	static public int hits = 0;
	static public int currentThreads = 0;
	static public Object sync = new Object();
	static private Thread t = null;
	static private Request req = null;
	static private BufferedReader in = null;
	static private int maxThreads;

	public static void main(String[] args) {
		try {
			surl = args[0];
			maxThreads = Integer.parseInt(args[2]);
	
			in = new BufferedReader(new FileReader(args[1]));
			System.out.println("Start Hitfaker for URL: " + surl + "\n");

			while ((proxy = in.readLine()) != null) {
				synchronized (sync) {
					if (currentThreads > maxThreads)
						sync.wait();
				}

				port = Integer.parseInt(proxy.substring(proxy.indexOf(':') + 1,
						proxy.length()).trim());
				proxy = proxy.substring(0, proxy.indexOf(':')).trim();

				req = new Request(surl, proxy, port);
				t = new Thread(req);
				currentThreads++;
				t.start();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("FINISHED THE PROXY LIST WITH " + hits + " HITS \n");

	}

}