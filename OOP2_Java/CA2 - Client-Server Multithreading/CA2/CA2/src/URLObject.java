import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLObject {
	
	public String url;
	public String html;
	
	public URLObject(String url) {
		this.url = url;
	}
	
	public String getHTML() {
		
		String outHTML = "";
		
		try {
			URL u = new URL(this.url);
			BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
			
			String inputLine;
			while((inputLine = in.readLine()) != null) {
				outHTML += inputLine;				
			}
			in.close();
		} catch (Exception e) {
		
			System.out.println("Get HTML page error");
		} 
		
		return outHTML;
	}
	
}
