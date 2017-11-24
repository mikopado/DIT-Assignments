import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class URLRunnable implements Runnable {
	
	public Socket clientSocket;
	public Cache cache;
	
	public URLRunnable(Socket client, Cache cache) {
		clientSocket = client;
		this.cache = cache;
	}
	@Override
	public void run() {
		
		
		String html = "";
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			if(in.readLine().equals("start")) {
				
				String url = in.readLine();				
				URLObject urlObject = new URLObject(url);				
				
				boolean noCache = Boolean.parseBoolean(in.readLine().toLowerCase());	
				// If user enters true get markup for requested url. 
				if(noCache) {
					html = urlObject.getHTML();
					if(!html.isEmpty()) { // Prevents to add the url when user enters an incorrect url
						cache.addURL(url, html);
					}
					
				}	
				// If user enters false or any word but true, find the url in the cache. 
				// If no url in cache get markup from requested url
				else { 
					html = cache.searchCache(url);
					if(html.isEmpty()) {
						html = urlObject.getHTML();
						cache.addURL(url, html);
					}
				}
				
				// if url is found send to the client the markup html of the page
				if(!html.isEmpty()) {
					out.println(html);
				}
				else {
					out.println("No page found!");
				}
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
