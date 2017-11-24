
public class SmsService implements IMessageService {

	@Override
	public void sendMessage(String from, String to, String msg) {
	
		System.out.println("from " + from + " to " + to + " " + msg);		
	}

}
