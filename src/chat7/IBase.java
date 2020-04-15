package chat7;

public interface IBase {
	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORACLE_URL = "jdbc:oracle:thin://@localhost:1521:orcl";
	
	void connect(String user, String pass);
	void execute();
	String executetwo();
	void check();
	void close();
	String scanValues(String title);
}
