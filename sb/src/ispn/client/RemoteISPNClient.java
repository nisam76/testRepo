package ispn.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryRemoved;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryRemovedEvent;

@ClientListener
public class RemoteISPNClient {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	RemoteCache<String, String> cache = null;
	public RemoteISPNClient() {
		
	}
	
	
	public void startClient() {
		Configuration config = new ConfigurationBuilder().addServer().host("127.0.0.1").port(9999).build();
		RemoteCacheManager cacheManager = new RemoteCacheManager(config);
		cache = cacheManager.getCache("dumpster");
		Calendar cal = Calendar.getInstance();
		System.out.println(cache.get("K")); // V
		cache.addClientListener(this);
	}
	
	public static void main(String[] args) {
		RemoteISPNClient client = new RemoteISPNClient();
		client.startClient();
	}
	
	@ClientCacheEntryCreated
	   public void handleCreatedEvent(ClientCacheEntryCreatedEvent<Object> e) {
		Calendar cal = Calendar.getInstance();
	      System.out.println(dateFormat.format(cal.getTime()));
	      System.out.println(" Add "+e.getKey()+ "   "+cache.get(e.getKey()));
	      
	   }

	   @ClientCacheEntryModified
	   public void handleModifiedEvent(ClientCacheEntryModifiedEvent<Object> e) throws InterruptedException {
		   Calendar cal = Calendar.getInstance();
	      System.out.println(dateFormat.format(cal.getTime()));
	      System.out.println("Updated key -> "+e.getKey() +" version -> "+e.getVersion()+"  data -> "+cache.get(e.getKey()));
	      System.out.println("Adding same element again"+e.getKey() +" with version "+e.getVersion());
	      cache.replaceWithVersion(""+e.getKey(), "NEW VALUE ---", e.getVersion());
	      Thread.sleep(3000);
	      System.out.println("Adding same element again"+e.getKey() +" with new version "+1011);
	     //cache.replaceWithVersion(""+e.getKey(), "NEW VALUE ---",1011);
	      cache.put(""+e.getKey(), cache.get(e.getKey())+"2222");
	      //System.out.println(cache.put(""+e.getKey(), cache.get(e.getKey())));
	   }

	   @ClientCacheEntryRemoved
	   public void handleRemovedEvent(ClientCacheEntryRemovedEvent<Object> e) {
		   Calendar cal = Calendar.getInstance();
		   System.out.println(dateFormat.format(cal.getTime()));
	      System.out.println("Deleting "+e.getKey());// +cache.get(e.getKey()));
	   }
}
