package chat7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IBaseimpl implements IBase{

	public PreparedStatement psmt;
	public Connection con;
	public ResultSet rs;
	
	public IBaseimpl() {}

	public IBaseimpl(String user, String pass) {
		try {
			Class.forName(ORACLE_DRIVER);
			connect(user, pass);
			
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	public void connect(String user, String pass) {
		try {
			con = DriverManager.getConnection(ORACLE_URL,user,pass);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	public void close() {
		try {
			if(con!=null)con.close();
			if(psmt!=null)psmt.close();
			if(rs!=null)rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	public String scanValues(String title) {
		Scanner sc = new Scanner(System.in);
		System.out.println(title);
		String inputStr = sc.nextLine();
		
		return inputStr;
	}
	@Override
	public void execute() {}
	@Override
	public void executetwo() {}
}
