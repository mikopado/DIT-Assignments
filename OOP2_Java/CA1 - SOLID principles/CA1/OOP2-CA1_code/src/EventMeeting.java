import java.util.ArrayList;
import java.util.List;

//Class that is responsible to invite people to the event 
public class EventMeeting {
	
	private List<IPersonDetails> attendees;
	private String eventName;
	
	public EventMeeting(String name){		
		attendees = new ArrayList<>();
		setEventName(name);
	}
	
	//Getter and Setter for Event name
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String value) {
		eventName = value;
	}
	
	//Push attendee to the list attendees
	public void addAttendee(IPersonDetails person){
		attendees.add(person);
	}

	public List<IPersonDetails> getAttendees(){
		return attendees;
	}
	
	//Send message to attendees at the end of event
	public void sendMessage(IMessageService msg, String from, String to, String message){
		msg.sendMessage(from, to, message);
	}
}
