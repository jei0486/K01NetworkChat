package chat7;


public class InsertQuery extends IBaseimpl{
	
	String name;
	String message;

	public InsertQuery(String name, String message) {
		super("kosmo", "1234");
		this.name = name;
		this.message = message;
	}
	public InsertQuery(String name) {
		this.name = name;
	}
	@Override
	public void execute() {

		try { 
			String query = "INSERT INTO  chating_tb  "
					+ " VALUES (seq_chating_tb_id.nextval,"
					+ "  ? ,? ,to_char( sysdate, 'yyyy.mm.dd hh24:mi'))";
			
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, name);
			psmt.setString(2, message);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected + "행이 입력되었습니다.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close();
		}
	}
	@Override
	public void check() {
		
		try { 
			String query = "INSERT INTO username VALUES (name)";
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, name);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected + "행이 입력되었습니다.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close();
		}
	}
}
