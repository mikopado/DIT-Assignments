
public class ServerMain {

	public static void main(String[] args) {
	
		System.out.println("Server Starts");
		SocketServer server = new SocketServer();
		server.runServer();
	}
	
}
