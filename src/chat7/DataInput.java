package chat7;

import java.util.Scanner;


public class DataInput extends IBaseimpl{

	public DataInput() {
		super("kosmo", "1234");
	}
	@Override
	public void execute() {//입력

		try { 
			String query = "INSERT INTO  chating_tb "
					+ " VALUES (seq_chating_tb_id.nextval, "
					+ " ? ,? ,"
					+ "to_char( regidate, 'yyyy.mm.dd hh24:mi')";
			
			psmt = con.prepareStatement(query);
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("대화명:");String name = sc.nextLine();
			System.out.print("대화내용:");String message = sc.nextLine();
//			String regidate = rs.getString("regidate").substring(0,13);
			
			psmt.setString(2, name);
			psmt.setString(3, message);
//			psmt.setString(3, regidate);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected + "행이 입력되었습니다.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close();
		}
	}
	public static void main(String[] args) {
		new DataInput().execute();
	}
}
