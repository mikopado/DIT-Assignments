import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

//Class that handles the data storage using file

public class FileStorage implements IStorage {
	
	private FileWriter fw; 
	private final String COMMA_SEPARATOR = ",";
	
	public FileStorage(String fileName){
		//Create a new file if this doesn't exist otherwise append data to an existing one
		try {
			
			File file = new File(fileName);
			
			if(file.exists()) {
				
				fw = new FileWriter(file, true);
				
			}else {
				
				fw = new FileWriter(file);
				
			}	
			
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} 

	}
	
	@Override
	public <T> void initializeStorage(List<T> params) {
		//Basically it creates the headline to the file 
		StringBuilder sb = new StringBuilder();
		
		for(T par : params) {
			
			sb.append(par + COMMA_SEPARATOR);
			
		}
		try {
			
			fw.append(sb.toString() + "\n");
			
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}	
	
	//Create a record so a row in the file 
	private String createRecordToStore(String record){	
		
		String[] values = record.toString().split("\\s+|,");
		StringBuilder sb = new StringBuilder();
		
		for(String val : values) {
			sb.append(val + COMMA_SEPARATOR);
		}
		return sb.toString() + "\n";
	}

	@Override
	public <T> void storeData(List<T> records) {
		
		for(T row: records){
			
			try {		
				
				fw.append(createRecordToStore(row.toString()));
				
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		closeFile();
		
	}
	
	
	private void closeFile() {

		try {
			
			fw.flush();
			fw.close();
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	
}
