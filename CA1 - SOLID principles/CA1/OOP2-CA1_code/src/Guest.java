
public class Guest extends Person{
	
	private String company;
	private String contact;

	public Guest(String fName, String lName, String email, String mobileNo, String company, String contact) {
		this(fName, lName, email, mobileNo);
		
		this.company = company;
		this.contact = contact;
	}
	public Guest(String fName, String lName, String email, String mobileNo) {
		super(fName, lName, email, mobileNo);
		
		
	}

}
