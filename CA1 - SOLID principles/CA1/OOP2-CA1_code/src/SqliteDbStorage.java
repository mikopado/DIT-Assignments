import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.List;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqliteDbStorage implements IStorage {
	
	private Connection conn;
	private Statement statem;
	private String tableName;
	private String dbName;
	
	
	public SqliteDbStorage(String dbName, String tableName){
		//Connect to a database or create new one
		this.tableName = tableName;
		this.dbName = dbName;
		this.conn = connect();
	}
	
	private String getDbName() {
		return this.dbName;
	}

	private String getTableName(){		
		return tableName;
	}
	
	//Create database connection
	private Connection connect() {
		Connection c = null;
		try{
			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + getDbName() + ".sqlite");
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return c;
	}
	
	//Can execute INSERT, UPDATE AND DELETE statements
	private void executeQuery(String query){
		
		try{
			
			this.statem = conn.createStatement();
			statem.executeUpdate(query);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//Create table query from a list of parameters: table name and attributes
	private <T> String createTable(List<T> table) {
		
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS " + getTableName() + "(");
		
		for(int i = 0; i < table.size(); i++) {			
			sb.append(table.get(i));
			if(i != table.size() - 1) {
				sb.append(",");
			}			
		}
		
		return sb.toString() + ")";
	}
	
	
	@Override
	//Create table into database
	public <T> void initializeStorage(List<T> params) {		
		
		executeQuery(createTable(params));
	}
	
	@Override
	public <T> void storeData(List<T> data) {
		//Get the last id value into table
		int primaryKey = getIdCounter();
		
		for(T item: data) {	
			//For each element in the list it takes the values corresponding to each column into database and
			// create a query string to use to insert data into database based on its schema
			
			String[] values = item.toString().split("\\s+|,");
			StringBuilder sb = new StringBuilder("VALUES(" + (++primaryKey) + ",");
			
			for(int i=0; i < values.length; i++) {	
				
				sb.append("'" + values[i] + "'");
				
				if(i != values.length - 1) {
					sb.append(",");
				}
			}
			
			sb.append(")");		
			executeQuery("INSERT INTO " + getTableName() + " " + sb.toString());
		}

		closeConnection();
	}	

	private void closeConnection() {
		
		try {
			conn.close();
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
	}	
	
	//Get the id values to keep them consistently when database is updated   
	private int getIdCounter() {
		
		int lastId = 0;			
		try {
			ResultSet rs = statem.executeQuery("select max(id) from " + getTableName());			
			if(rs.getString(1) != null) {
				lastId = Integer.parseInt(rs.getString(1));
			}		
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		
		return lastId;
	}
}
