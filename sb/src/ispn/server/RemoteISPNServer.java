package ispn.server;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;

import ispn.client.RemoteISPNClient;

public class RemoteISPNServer {

	public RemoteISPNServer() {
		
	}
	
	
	public void startServer() {
		// Start a cache manager as usual
		EmbeddedCacheManager cacheManager = null;

		try {
//			InputStream in = ClassLoader.getSystemResourceAsStream("C://Users//MDAMODAR//Downloads//ISPN2//hotrod-ispn.xml");
//		    cacheManager = new DefaultCacheManager(in);
			
			cacheManager = new DefaultCacheManager("C://INFINI-HOTROD//ISPN2//ISPN2//hotrod-ispn.xml");
			RemoteISPNClient eClient = new RemoteISPNClient();
			
		} catch(IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		} finally {
			
		}
		
		//cacheManager = new DefaultCacheManager();
		

		// Start a server to allow remote access to the cache manager
		HotRodServerConfiguration serverConfig = new HotRodServerConfigurationBuilder().host("127.0.0.1").port(9999).build();
		HotRodServer server = new HotRodServer();
		server.start(serverConfig, cacheManager);

		// Start the example cache
		Cache<String, String> cache = cacheManager.getCache("dumpster", true);
		//cache.put("K", "V");
		//System.out.println(cache.get("K"));
		int i = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		//while(true){
			
			try {
				Thread.sleep(31000);
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));
				cache.put("K"+i, "V"+i);
				//cache.put("K"+i, "V"+i);
				if(i%5 == 0){
					cache.put("K"+i, "V1"+i);
					
					//Thread.sleep(2000);
					///cache.remove("K"+i);
					//cache.put("K"+i, "V1"+i);
					
				}
				//i++;
				Thread.sleep(10000);
				cache.put("K"+i, "V"+i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
	
	
	public static void main(String[] args) {
		RemoteISPNServer server = new RemoteISPNServer();
		server.startServer();		
	}
}
