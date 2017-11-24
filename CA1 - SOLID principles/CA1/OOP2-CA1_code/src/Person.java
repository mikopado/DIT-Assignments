
public class Person implements IPersonDetails {
	
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNo;
	
	public Person(String fName, String lName, String email, String mobileNo){
		firstName = fName;
		lastName = lName;
		this.email = email;
		this.mobileNo = mobileNo;
	}
	
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getEmail(){
		return email;
	}
	public String getMobileNo(){
		return mobileNo;
	}
	
	public String toString() {
		return getFirstName() + " " + getLastName() + " " + getEmail() + " " + getMobileNo();
	}
	
	
	@Override
	public String getNameDetails() {		
		return getFirstName() + ", " + getLastName();
	}

	@Override
	public String getEmailContact() {
		return getEmail();
	}

	@Override
	public String getPhoneContact() {
		return getMobileNo();
	}
	
	
}
