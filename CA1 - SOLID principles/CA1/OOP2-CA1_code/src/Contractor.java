import java.time.LocalDate;

public class Contractor extends Person {
	
	private LocalDate dob;
	private String company;
	private String contact;

	public Contractor(String fName, String lName, String email, String mobileNo, 
						LocalDate dob, String company, String contact) {
		this(fName, lName, email, mobileNo);
		
		this.dob = dob;
		this.company = company;
		this.contact = contact;
	}
	public Contractor(String fName, String lName, String email, String mobileNo) {
		super(fName, lName, email, mobileNo);
	
		
	}
	
}
