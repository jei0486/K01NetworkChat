package chat7;

public class SelectQuery extends IBaseimpl{
	
	public SelectQuery() {
		super("kosmo","1234");
	}
	@Override
	public String executetwo() {
		
		try {
				String sql = "SELECT * FROM  username "
						+" WHERE name = ? ";
				psmt = con.prepareStatement(sql);
				psmt.setString(1, scanValues("찾는 이름"));
				rs = psmt.executeQuery();
				while (rs.next()) {
					String name = rs.getString(1);
					return name;
				}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
		finally {
			close();
		}
		return null;
	}
}
