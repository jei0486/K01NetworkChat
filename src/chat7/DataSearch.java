package chat7;

public class DataSearch extends IBaseimpl{
	
	public DataSearch() {
		super("kosmo","1234");
	}
	//전체출력
	@Override
	public void executetwo() {
		try {
			String sql = "SELECT * FROM  chating_tb ";

			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				
				String name = rs.getString(2);
				String message = rs.getString(3);
				String regidate = rs.getString("regidate").substring(0,13);

				System.out.printf("대화명: %s 대화내용 : %s 현재시간 : %d \n", name, message, regidate);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close();
		}
	}
}
