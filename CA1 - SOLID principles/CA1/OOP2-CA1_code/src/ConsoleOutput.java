import java.util.List;

//Class to display Person details on the console. Implements IOutput interface

public class ConsoleOutput implements IOutput {
	
	@Override
	public void displayItems(List<IPersonDetails> items) {
	
		for(IPersonDetails person: items){
			System.out.println(person.getNameDetails() + ", " + person.getPhoneContact());
		}
	}	

}
