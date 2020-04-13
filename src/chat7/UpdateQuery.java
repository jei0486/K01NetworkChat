package chat7;

import java.sql.SQLException;
import java.util.Scanner;

public class UpdateQuery extends IBaseimpl{

	public UpdateQuery() {
		super("kosmo", "1234");
	}
	
	@Override
	public void execute() {

		String sql = "UPDATE chating_tb "
				+" SET  message"
				+" WHERE name=?";
		try {
			psmt = con.prepareStatement(sql);
				Scanner sc = new Scanner(System.in);
				System.out.print("대화명:"); String name = sc.nextLine();
				System.out.print("대화내용 :"); String message = sc.nextLine();
				psmt.setString(1, name);
				psmt.setString(2, message);
				
				int affected = psmt.executeUpdate();
				System.out.println(affected + "행이 업데이트 되었습니다.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			close();
		}
	}
}
