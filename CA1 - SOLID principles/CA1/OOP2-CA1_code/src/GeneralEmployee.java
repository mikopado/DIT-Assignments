import java.time.LocalDate;

public class GeneralEmployee extends Person {
	
	private LocalDate dob;
	private double salary;
	private String jobTitle;

	public GeneralEmployee(String fName, String lName, String email, String mobileNo,
							LocalDate dob, double salary, String jobTitle) {
		this(fName, lName, email, mobileNo);
		
		this.dob = dob;
		this.salary = salary;
		this.jobTitle = jobTitle;
	}

	public GeneralEmployee(String fName, String lName, String email, String mobileNo) {
		super(fName, lName, email, mobileNo);
		
	}

}
