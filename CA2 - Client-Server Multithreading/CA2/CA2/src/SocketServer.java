import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	
	private int portNo = 55555;
	private Cache cache; // declare Cache in Socket Server to keep cache available as long as server is running 
	
	public void runServer() {
		
		try {
			ServerSocket server = new ServerSocket(portNo);
			cache = new Cache();
			while(true) {
				Socket clientSocket = server.accept();
				new Thread(new URLRunnable(clientSocket, cache)).start();
			}
		} catch (IOException e) {
	
			e.printStackTrace();
		}
	}
}
