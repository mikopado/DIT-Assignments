import java.util.ArrayList;
import java.util.List;

// Entry point of the program, where the main method takes place

public class EntryPoint {


	public static void main(String[] args) {
	
		//Create Event Meeting and ad some attendees to the event
		EventMeeting event = new EventMeeting("Job Event");
		event.addAttendee(new GeneralEmployee("John", "Roberts", "j.roberts@gmail.com", "08963456"));
		event.addAttendee(new Contractor("Alvin", "Michel", "a.michel@gmail.com", "08632569"));
		event.addAttendee(new Guest("Bob", "Robinson", "b.robinson@gmail.com", "08745698"));
		event.addAttendee(new Guest("Jack", "Clark", "j.clark@gmail.com", "08747896"));
		
		
		//Create a file to store attendees 
		IStorage storage = new FileStorage("event-attendees.csv");
		List<String> headline = new ArrayList<String>();
		headline.add("First Name");
		headline.add("Last Name");
		headline.add("Email");
		headline.add("Mobile No.");
		//Initialize storage will create a new csv file with the headline as first row
		storage.initializeStorage(headline);
		//Store all attendees who take part to event into the event-attendees.csv file
		storage.storeData(event.getAttendees());
		//Test if it is possible to append more attendees to the same file
		IStorage storage2 = new FileStorage("event-attendees.csv");
		storage2.storeData(event.getAttendees());
		
		
		//Create a database named Event, so it will create a file Event.sqlite
		IStorage dbStorage = new SqliteDbStorage("Event", "Attendees");
		//Create an Attendees table in database with id, first name, last name, email and mobile no as columns
		List<String> table = new ArrayList<String>();		
		table.add("id integer primary key");		
		table.add("firstname varchar(80) not null");		
		table.add("lastname varchar(80) not null");		
		table.add("email varchar(80) not null");	
		table.add("mobileNo varchar(80) not null");			
		dbStorage.initializeStorage(table);		
		//Insert event attendees data into table
		dbStorage.storeData(event.getAttendees());
		
		
		//Test if insert more data, first data is inserted and secondly the primary key keeps incrementing
		dbStorage = new SqliteDbStorage("Event", "Attendees");
		dbStorage.initializeStorage(table);	
		dbStorage.storeData(event.getAttendees());
		
		
		
		//Display Attendees details on Console
		IOutput out = new ConsoleOutput();
		out.displayItems(event.getAttendees());
		
		
		//Sending messages to attendees either by email or sms
		IMessageService email = new EmailService();
		IMessageService sms = new SmsService();
		for(IPersonDetails person: event.getAttendees()) {
			event.sendMessage(email, event.getEventName(), person.getEmailContact(), "Event is over. Thank you!");
			event.sendMessage(sms, event.getEventName(), person.getPhoneContact(), "Event is over. Thank you!");
		}
	}

}
