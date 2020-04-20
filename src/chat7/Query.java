package chat7;


public class Query extends IBaseimpl{
	
	String name;
	String message;

	public Query(String name, String message) {
		super("kosmo", "1234");
		this.name = name;
		this.message = message;
	}
	public Query(String name) {
		super("kosmo", "1234");
		this.name = name;
	}
	@Override
	public void execute() {//chating_tb에 이름 메세지 연동

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
	public boolean check() {//중복되는 이름을 검사하는 쿼리문
		
		try { 
			String query = "INSERT INTO username (name) VALUES (?)";
			//에러 확인용
			//System.out.println("쿼리문 진입");
			//System.out.println(query);
			psmt = con.prepareStatement(query);
			psmt.setString(1, name);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected + "행이 입력되었습니다.");
			return true;
		} catch (Exception e) {
			//e.printStackTrace();///////////프로그램 완성시 주석처리 할것
			System.out.println("접속할수 없는 이름입니다.");
			return false;
		}
		finally {
			close();
		}
	}
	@Override
	public boolean black() {//블랙리스트  update문
		//UPDATE username SET blacklist = name WHERE name = 'vvv';
		//"INSERT INTO username (blacklist) VALUES (?)";
		try { 
			String query = "UPDATE username SET blacklist = name WHERE name = ?";
			//에러 확인용
			//System.out.println("쿼리문 진입");
			//System.out.println(query);
			psmt = con.prepareStatement(query);
			psmt.setString(1, name);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected + "행이 입력되었습니다.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();///////////프로그램 완성시 주석처리 할것
			return false;
		}
		finally {
			close();
		}
	}
	@Override
	public void delete() {//채팅에서 나갈시 테이블에서 일반유저이름만 삭제 //블랙리스트는 남김
		try {
			//2.쿼리문 미리 준비
			String query = "DELETE FROM username WHERE name = ? and blacklist is null";
			//3.prepared 객체 생성
			psmt = con.prepareStatement(query);
			//4.인파라미터 값 설정
			psmt.setString(1, name);
			//5.쿼리 실행 후 결과값 반환
			int affected = psmt.executeUpdate();
			System.out.println(affected + "행이 입력되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
}
