import java.util.ArrayList;

public class Cache {
	
	ArrayList<URLObject> pages;
	
	public Cache(){
		pages = new ArrayList<URLObject>();
	}	
	
	public synchronized String searchCache(String urlRequest) {
		
		String response = "";
		for(int i = 0; i < pages.size(); i++) {
			
			URLObject url = pages.get(i);
			
			if(url.url.equals(urlRequest)) {
				response = url.html;
				break;
			}
		}
		
		return response;
	}
	
	public synchronized void addURL(String url, String html) {
		
		URLObject urlObject = new URLObject(url);		
		urlObject.html = "[Cached]" + html;
		
		if(!pages.contains(urlObject)) {
			pages.add(urlObject);
		}		
		
	}
	
}
