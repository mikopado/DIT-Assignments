import java.util.List;

public interface IStorage {
	
	public <T> void storeData(List<T> data);
	public <T> void initializeStorage(List<T> params);
	
}
