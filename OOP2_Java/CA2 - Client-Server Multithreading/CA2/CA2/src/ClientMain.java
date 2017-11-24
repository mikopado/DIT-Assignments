import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int portNo = 55555;
		String IP = "127.0.0.1";
		
		try {
			Socket clientSocket = new Socket(IP, portNo);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			
			out.println("start");
			System.out.println("Enter URL: ");
			out.println(userIn.readLine());
			System.out.println("Do not return from cache? (enter True or False) ");
			out.println(userIn.readLine());
			System.out.println(in.readLine());
			
			clientSocket.close();
			
		} catch (UnknownHostException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}

}
